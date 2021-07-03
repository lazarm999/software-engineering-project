package com.parovi.zadruga.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.parovi.zadruga.App;
import com.parovi.zadruga.ChatNotification;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserChat;
import com.parovi.zadruga.models.requestModels.ChatMembersRequest;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import org.jetbrains.annotations.NotNull;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository extends BaseRepository {

    public void connectToChatServer(MutableLiveData<CustomResponse<?>> isConnected){
        User u = Utility.getLoggedInUser(App.getAppContext());
        String pass = Utility.getLoggedInUserPassword(App.getAppContext());
        QBUser qbUser = new QBUser();
        qbUser.setEmail(u.getEmail());
        qbUser.setPassword(pass);
        qbUser.setId(u.getUserQbId());
        QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Log.i("connectToChatServer", "braoooo");
                isConnected.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("connectToChatServer", "NE braoooo");
                isConnected.postValue(new CustomResponse<>(CustomResponse.Status.OK, e.getMessage()));
            }
        });
    }

    public void getUserByQbId(){

    }

    private Chat basicQbChatDialogToChat(QBChatDialog qbChat){
        //treba ovde api poziv da bih mogaao da izvucem info o korisniku koji je zadnji poslao poruku
        Chat chat;
        Utility.ChatType type;
        if(qbChat.getType() == QBDialogType.PRIVATE)
            type = Utility.ChatType.PRIVATE;
        else
            type = Utility.ChatType.GROUP;
        chat = new Chat(qbChat.getDialogId(), type, "", qbChat.getOccupants().size(),
                qbChat.getLastMessage(),
                qbChat.getLastMessageDateSent(),qbChat.getCreatedAt(), qbChat);
        return chat;
    }

    private Message qbChatMessageToMessage(QBChatMessage qbMess){
        return new Message(qbMess.getId(), qbMess.getSenderId(), (String) qbMess.getProperty("username"),
                new Date(qbMess.getDateSent()), qbMess.getBody(), qbMess.getDialogId(), qbMess);
    }

    private int getChatIndexByQbId(List<Chat> chats, String qbChatId){
        if(chats != null){
            for (int i = 0; i < chats.size(); i++) {
                if(chats.get(i).getChatId().equals(qbChatId)){
                    return i;
                }
            }
        }
        return -1;
    }

    public void getAllChats(MutableLiveData<CustomResponse<?>> chats){
        int userId = Utility.getLoggedInUserQbId(App.getAppContext());
        Boolean[] isSynced = {false};
        getAllChatsLocal(chats, userId, isSynced);

        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(50);
        requestBuilder.sortAsc("last_message_date_sent");

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChats, Bundle params) {
                Utility.getExecutorService().execute(() -> {
                    List<Chat> tmpChats = new ArrayList<>();
                    for (QBChatDialog qbChat : qbChats) {
                        Chat chat = fullQbChatToChat(qbChat, chats);
                        tmpChats.add(chat);
                        DaoFactory.getChatDao().insertOrUpdate(chat);
                    }
                    chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChats));
                });
            }
            @Override
            public void onError(QBResponseException e) {
                Log.i("insertChat", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), chats);
            }
        });
    }

    private Chat fullQbChatToChat(QBChatDialog qbChat, MutableLiveData<CustomResponse<?>> res){
        final String token = Utility.getAccessToken(App.getAppContext());
        int userId = Utility.getLoggedInUserQbId(App.getAppContext());
        int userQbId = Utility.getLoggedInUserQbId(App.getAppContext());
        String qbChatId = qbChat.getDialogId();
        //Utility.ChatType type = qbChat.getType() == QBDialogType.PRIVATE ? Utility.ChatType.PRIVATE : Utility.ChatType.GROUP;
        Chat chat = basicQbChatDialogToChat(qbChat);
        saveChatLocally(qbChatToChat(qbChat));
        try {
            Response<Ad> ad = ApiFactory.getAdApi().getAdByQbChatId(Utility.getAccessToken(App.getAppContext()), qbChatId).execute();
            if (ad.isSuccessful()) {
                if(ad.body() != null){
                    String chatTitle = ad.body().getTitle();
                    chat.setChatTitle(chatTitle);
                    chat.setFkAdId(ad.body().getAdId());
                }
            } else
                responseNotSuccessful(ad.code(), res);
        } catch (IOException e) {
            e.printStackTrace();
            apiCallOnFailure(e.getMessage(), res);
        }
        if (chat.getFkAdId() == null) {
            int chatterQbId = qbChat.getOccupants().get(0) == userQbId ? qbChat.getOccupants().get(1) : qbChat.getOccupants().get(0);
            try {
                Response<User> user = ApiFactory.getUserApi().getUserByQbUserId(token, chatterQbId).execute();
                Response<ResponseBody> profileImage = ApiFactory.getUserApi().getProfilePictureByUserQbId(token, chatterQbId).execute();
                if (user.isSuccessful() && user.body() != null) {
                    String chatTitle = user.body().getFirstName() + " " + user.body().getLastName();
                    chat.setChatTitle(chatTitle);
                } else
                    responseNotSuccessful(user.code(), res);
                if (profileImage.isSuccessful() && profileImage.body() != null) {
                    Bitmap bmImage = BitmapFactory.decodeStream(profileImage.body().byteStream());
                    chat.setProfileImage(bmImage);
                    saveImageLocally(bmImage, userId);
                } else
                    responseNotSuccessful(profileImage.code(), res);
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), res);
            }
        } else {
            try {
                Response<Ad> ad = ApiFactory.getAdApi().getAdByQbChatId(Utility.getAccessToken(App.getAppContext()), qbChatId).execute();
                if (ad.isSuccessful() && ad.body() != null) {
                    String chatTitle = ad.body().getTitle();
                    chat.setChatTitle(chatTitle);
                    chat.setFkAdId(ad.body().getAdId());
                } else
                    responseNotSuccessful(ad.code(), res);

            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), res);
            }
        }
        if (qbChat.getLastMessageUserId() != null) {
            try {
                Response<User> lastMessageSender = ApiFactory.getUserApi().getUserByQbUserId(token, qbChat.getLastMessageUserId()).execute();
                if (lastMessageSender.isSuccessful() && lastMessageSender.body() != null) {
                    String lastSenderName = lastMessageSender.body().getUsername();
                    chat.setLastSenderUsername(lastSenderName);
                } else
                    responseNotSuccessful(lastMessageSender.code(), res);
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), res);
            }
        }
        return  chat;
    }

    public void getAllChatsLocal(MutableLiveData<CustomResponse<?>> chats, int userId, Boolean[] isSynced){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<Chat> localChats = DaoFactory.getChatDao().getAllChatsByUserId(userId);
                if(localChats != null){
                    Bitmap chatImage = null;
                    for (Chat chat : localChats) {
                        if(chat.getType() == Utility.ChatType.PRIVATE){
                            if(chat.getFkChatterId() != null)
                                chatImage = getProfilePictureLocal(chat.getFkChatterId());
                            if(chatImage != null)
                                chat.setProfileImage(chatImage);
                        }
                    }
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, localChats));
                    }
                }
            }
        });
    }

    public void updateChat(MutableLiveData<CustomResponse<?>> chats, String chatId){
        final String token = Utility.getAccessToken(App.getAppContext());
        QBRestChatService.getChatDialogById(chatId).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                ApiFactory.getUserApi().getUserByQbUserId(token, qbChatDialog.getLastMessageUserId()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                        boolean isUpdated = false;
                        List<Chat> tmpChatList = (List<Chat>) chats.getValue().getBody();
                        int i;
                        if((i = getChatIndexByQbId(tmpChatList, chatId)) != -1){
                            Chat updatedChat = tmpChatList.get(i);
                            String lastMsg = qbChatDialog.getLastMessage();
                            long lastDate = qbChatDialog.getLastMessageDateSent();
                            updatedChat.setLastMessage(lastMsg);
                            updatedChat.setLastMessageDateSent(lastDate);
                            if(response.isSuccessful() && response.body() != null) {
                                updatedChat.setLastSenderUsername(response.body().getUsername());
                            }
                            tmpChatList.remove(i);
                            tmpChatList.add(tmpChatList.size(), updatedChat);
                            isUpdated = true;
                            Utility.getExecutorService().execute(new Runnable() {
                                @Override
                                public void run() {
                                    DaoFactory.getChatDao().updateLastMessage(lastMsg, chatId);
                                    DaoFactory.getChatDao().updateLastMessageDateSent(lastDate, chatId);
                                }
                            });
                        }
                        if(!isUpdated){
                            Chat newChat = basicQbChatDialogToChat(qbChatDialog);
                            tmpChatList.add(tmpChatList.size() - 1, newChat);
                            Utility.getExecutorService().execute(new Runnable() {
                                @Override
                                public void run() {
                                    DaoFactory.getChatDao().insertOrUpdate(newChat);
                                }
                            });
                        }
                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChatList));
                    }

                    @Override
                    public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                responseNotSuccessful(e.getHttpStatusCode(), chats);
            }
        });
    }

    public void updateMessages(MutableLiveData<CustomResponse<?>> messages, QBChatMessage qbChatMessage){
        List<Message> tmpList = new ArrayList<>();
        if(messages.getValue() != null){
            tmpList = (List<Message>) messages.getValue().getBody();
        }
        tmpList.add(qbChatMessageToMessage(qbChatMessage));
        messages.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpList));
    }

    private void saveUserChatLocally(List<User> chatMembers, String dialogId) {
        for (User u: chatMembers) {
            DaoFactory.getUserChatDao().insertOrUpdate(new UserChat(dialogId, u.getUserQbId()));
        }
    }

    public void getChatMembers(MutableLiveData<CustomResponse<?>> chatMembers, QBChatDialog chat) {
        final String token = Utility.getAccessToken(App.getAppContext());
        ChatMembersRequest req = new ChatMembersRequest(chat.getOccupants());
        Boolean[] isSynced = new Boolean[]{false};
        getChatMembersLocal(chatMembers, chat.getDialogId(), isSynced);
        ApiFactory.getUserApi().getChatMembers(token, req).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NotNull Call<List<User>> call, @NotNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    synchronized (isSynced[0]){
                        chatMembers.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                    }
                    for (User user : response.body()) {
                        int userId = user.getUserId();
                        MutableLiveData<CustomResponse<?>> profileImg = new MutableLiveData<>();
                        Observer<CustomResponse<?>> imageObserver = new Observer<CustomResponse<?>>() {
                            @Override
                            public void onChanged(CustomResponse<?> customResponse) {
                                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                                    Bitmap bmImage = (Bitmap) customResponse.getBody();
                                    List<User> users = (List<User>) chatMembers.getValue().getBody();
                                    if (users != null) {
                                        for (int i = 0; i < users.size(); i++) {
                                            if (users.get(i).getUserId() == userId) {
                                                if (bmImage != null)
                                                    users.get(i).setProfileImage(bmImage);
                                                break;
                                            }
                                        }
                                    }
                                    if(bmImage != null)
                                        saveImageLocally(bmImage, userId);
                                    chatMembers.postValue(new CustomResponse<>(CustomResponse.Status.OK, users));
                                }
                                if (!customResponse.isLocal())
                                    profileImg.removeObserver(this);
                            }
                        };
                        profileImg.observeForever(imageObserver);
                        getProfilePicture(profileImg, userId);
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveUserLocally(response.body());
                            saveUserChatLocally(response.body(), chat.getDialogId());
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), chatMembers);
            }

            @Override
            public void onFailure(@NotNull Call<List<User>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), chatMembers);
            }
        });
    }

    private void getChatMembersLocal(MutableLiveData<CustomResponse<?>> chatMembers, String qbChatId, Boolean[] isSynced){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<User> localChatMembers = DaoFactory.getChatDao().getChatMembers(qbChatId);
                for (User user: localChatMembers) {
                    user.setProfileImage(getProfilePictureLocal(user.getUserId()));
                }
                synchronized (isSynced[0]){
                    if(!isSynced[0])
                        chatMembers.postValue(new CustomResponse<>(CustomResponse.Status.OK, localChatMembers));
                }
            }
        });
    }

    public void getAdByChatId(MutableLiveData<CustomResponse<?>> ad, String qbChatId){
        final String token = Utility.getAccessToken(App.getAppContext());
        Boolean[] isSynced = new Boolean[]{ false };
        getAdByChatIdLocal(ad, qbChatId, isSynced);
        ApiFactory.getAdApi().getAdByQbChatId(token, qbChatId).enqueue(new Callback<Ad>() {
            @Override
            public void onResponse(@NotNull Call<Ad> call, @NotNull Response<Ad> response) {
                if(response.isSuccessful() && response.body() != null){
                    synchronized (isSynced[0]){
                        ad.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveAdLocally(response.body());
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), ad);
            }

            @Override
            public void onFailure(@NotNull Call<Ad> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), ad);
            }
        });
    }

    private void getAdByChatIdLocal(MutableLiveData<CustomResponse<?>> ad, String qbChatId, Boolean[] isSynced){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Ad adLocal = DaoFactory.getAdDao().getAdByQbChatId(qbChatId);
                if(adLocal != null){
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            ad.postValue(new CustomResponse<>(CustomResponse.Status.OK, adLocal));
                    }
                }
            }
        });
    }

    public void sendMessage(MutableLiveData<CustomResponse<?>> isSent, MutableLiveData<CustomResponse<?>> messages, MutableLiveData<CustomResponse<?>> chats,
                            QBChatDialog qbChat, String message, Integer adId){
        if(qbChat == null) return;
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                if(!qbChat.isJoined()){
                    try {
                        qbChat.join(new DiscussionHistory());
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException e) {
                        e.printStackTrace();
                    }
                }
                User u = Utility.getLoggedInUser(App.getAppContext());
                QBChatMessage qbMessage = new QBChatMessage();
                qbMessage.setBody(message);
                qbMessage.setSaveToHistory(true);
                qbMessage.setProperty("username", u.getUsername());
                qbMessage.setSenderId(u.getUserQbId());
                qbMessage.setDateSent((new Date()).getTime());
                qbMessage.setDialogId(qbChat.getDialogId());

                qbChat.sendMessage(qbMessage, new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        isSent.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                        updateMessages(messages, qbMessage);
                        updateChat(chats, qbChat.getDialogId());
                        List<Integer> ids = new ArrayList<>();
                        for (Integer id : qbChat.getOccupants()) {
                            if(id != u.getUserQbId())
                                ids.add(id);
                        }
                        sendChatNotification(new ChatNotification(u.getUsername(), message, adId,
                                ids, qbChat.getDialogId()));
                        //saveMessageLocally(qbMessage);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.i("sendMessage", "NE braoooo");
                        responseNotSuccessful(e.getHttpStatusCode(), isSent);
                    }
                });
            }
        });

    }

    private void saveMessageLocally(QBChatMessage qbMessage) {
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DaoFactory.getMessageDao().insertOrUpdate(qbChatMessageToMessage(qbMessage));
            }
        });
    }

    public void getMessages(MutableLiveData<CustomResponse<?>> messages, QBChatDialog chat, int messagesSkipped,
                            boolean shouldAppend){
        if(chat == null) return;
        Boolean[] isSynced = {false};
        //getMessagesLocal(messages, chat.getDialogId(), isSynced);
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(100);
        messageGetBuilder.setSkip(messagesSkipped);
        QBRestChatService.getDialogMessages(chat, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                List<Message> tmpMsgList;
                if(shouldAppend && messages.getValue() != null && messages.getValue().getBody() != null)
                    tmpMsgList = (List<Message>) messages.getValue().getBody();
                else
                    tmpMsgList = new ArrayList<>();
                for (QBChatMessage qbMsg: qbChatMessages) {
                    tmpMsgList.add(qbChatMessageToMessage(qbMsg));
                }
                messages.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpMsgList));
                Utility.getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        DaoFactory.getMessageDao().insertOrUpdate(tmpMsgList);
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("sendMessage", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), messages);
            }
        });
    }

    private void getMessagesLocal(MutableLiveData<CustomResponse<?>> messages, String qbChatId, Boolean[] isSynced) {
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<Message> localMessages = DaoFactory.getMessageDao().getMessagesByQbChatId(qbChatId);
                if(localMessages != null)
                    messages.postValue(new CustomResponse<>(CustomResponse.Status.OK, localMessages));
            }
        });
    }

    public void addOnMessageReceivedGlobal(QBChatDialogMessageListener newMessageListener){
        if(QBChatService.getInstance().getIncomingMessagesManager() != null)
            QBChatService.getInstance().getIncomingMessagesManager().addDialogMessageListener(newMessageListener);
    }

    public void removeGlobalMessageReceivedListener(QBChatDialogMessageListener newMessageListener){
        if(QBChatService.getInstance().getIncomingMessagesManager() != null)
            QBChatService.getInstance().getIncomingMessagesManager().removeDialogMessageListrener(newMessageListener);
    }

    public void getChatById(MutableLiveData<CustomResponse<?>> chat, String chatQbId){
        Utility.getExecutorService().execute(() -> {
            try {
                QBChatDialog qbChatDialog = QBRestChatService.getChatDialogById(chatQbId).perform();
                chat.postValue(new CustomResponse<>(CustomResponse.Status.OK, fullQbChatToChat(qbChatDialog, chat)));
            } catch (QBResponseException e) {
                e.printStackTrace();
                responseNotSuccessful(e.getHttpStatusCode(), chat);
            }
        });
    }
}