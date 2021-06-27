package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.repository.BaseRepository;
import com.parovi.zadruga.repository.ChatRepository;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private LiveData<QBChatDialog> chat;
    private final MutableLiveData<CustomResponse<?>> chats;
    private final MutableLiveData<QBChatMessage> newMessage;
    private final MutableLiveData<CustomResponse<?>> isSent;
    private final MutableLiveData<CustomResponse<?>> isConnected;
    private final MutableLiveData<CustomResponse<?>> messages;
    private final MutableLiveData<CustomResponse<?>> chatMembers;
    private final MutableLiveData<CustomResponse<?>> ad;
    private final int adId = 5;
    private final QBChatDialogMessageListener newMessageListener = new QBChatDialogMessageListener() {
        @Override
        public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
            if(qbChatMessage.getSenderId() != Utility.getLoggedInUserQbId(App.getAppContext())){
                rep.updateMessages(messages, qbChatMessage);
                //rep.updateChat(chats, dialogId);
            }
        }

        @Override
        public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer senderId) {
            //newMessage.postValue(null);
        }
    };

    private final ChatRepository rep;

    public ChatViewModel(@NonNull Application app) {
        super(app);
        rep = new ChatRepository();
        chats = new MutableLiveData<>();
        isConnected = new MutableLiveData<>();
        newMessage = new MutableLiveData<>();
        isSent = new MutableLiveData<>();
        messages = new MutableLiveData<>();
        chatMembers = new MutableLiveData<>();
        ad = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> observeIsConnected() {
        return isConnected;
    }

    public void connectToChatServer(){
        rep.connectToChatServer(isConnected);
    }

    public MutableLiveData<CustomResponse<?>> observeChats(){
        return chats;
    }

    public MutableLiveData<QBChatMessage> observeNewMessages(){
        return newMessage;
    }

    public void addOnGlobalMessageReceived(){
        rep.addOnMessageReceivedGlobal(newMessageListener);
    }

    public void sendMessage(String message){
        rep.sendMessage(isSent, messages, chats, getChatById("60b3e4a2ce5b150048cf9f62"), message, 3);
    }

    public void updateChat(){
        rep.updateChat(getChatById("60c3909b094ee2001e2e10c8"));
    }

    public void removeGlobalMessageListener(){
        rep.removeGlobalMessageReceivedListener(newMessageListener);
    }

    public void getAllChats(){
        rep.getAllChats(chats);
    }

    public void getMessages(){
        rep.getMessages(messages, ((List<Chat>)chats.getValue().getBody()).get(0).getQbChat(), 0, false);
    }

    public void getChatMembers(){
        rep.getChatMembers(chatMembers, ((List<Chat>)chats.getValue().getBody()).get(0).getQbChat());
    }

    public void getAd(){
        rep.getAdByChatId(ad, ((List<Chat>)chats.getValue().getBody()).get(0).getQbChat().getDialogId());
    }

    public LiveData<CustomResponse<?>> observeMessages() {
        return messages;
    }

    private QBChatDialog getChatById(String chatId){
        if(chats.getValue() != null && chats.getValue().getBody() != null){
            for (Chat chat : (List<Chat>)chats.getValue().getBody()) {
                if(chat.getChatId().equals(chatId)) return chat.getQbChat();
            }
        }
        return null;
    }

}
