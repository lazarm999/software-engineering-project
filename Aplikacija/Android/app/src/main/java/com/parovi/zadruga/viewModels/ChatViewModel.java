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
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    //connect to chat server -> adOnGlobalMessageReceived
    private String currChatId;
    private LiveData<QBChatDialog> chat;
    private MutableLiveData<CustomResponse<?>> chats;
    private MutableLiveData<QBChatMessage> newMessage;
    private MutableLiveData<CustomResponse<?>> isSent;
    private MutableLiveData<CustomResponse<?>> isConnected;
    private MutableLiveData<CustomResponse<?>> messages;
    private int adId = 5;
    private QBChatDialogMessageListener newMessageListener = new QBChatDialogMessageListener() {
        @Override
        public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
            if(currChatId.equals(dialogId))
                rep.updateMessages(messages, qbChatMessage);
            rep.updateChat(chats, dialogId, adId);
        }

        @Override
        public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer senderId) {
            //newMessage.postValue(null);
        }
    };

    private ZadrugaRepository rep;

    public ChatViewModel(@NonNull Application app) {
        super(app);
        rep = ZadrugaRepository.getInstance(app);
        chats = new MutableLiveData<>();
        isConnected = new MutableLiveData<>();
        newMessage = new MutableLiveData<>();
        isSent = new MutableLiveData<>();
        messages = new MutableLiveData<>();
        // loadChats()
        // viewHolder.OnClickListener -> fragment.OnListItemClicked { model.loadMessages() }; NavigateTo(chat_messages_fragment)
    }

    public MutableLiveData<CustomResponse<?>> observeIsConnected() {
        return isConnected;
    }

    public void connectToChatServer(){
        //rep.connectToChatServer(isConnected, u);
    }

    public MutableLiveData<CustomResponse<?>> observeChats(){
        return chats;
    }
    public MutableLiveData<QBChatMessage> observeNewMessages(){
        return newMessage;
    }
    public void adOnGlobalMessageReceived(){
        rep.addOnMessageReceivedGlobal(newMessageListener);
    }

    public void sendMessage(String message){
        rep.sendMessage(isSent, ((List<Chat>)chats.getValue().getBody()).get(0).getQbChat(), new User(3, 128586493), message);
    }

    public void removeGlobalMessageListener(){
        rep.removeGlobalMessageReceivedListener(newMessageListener);
    }

    public void getAllChats(){
        rep.getAllChats(chats, Utility.getUserId(App.getAppContext()));
    }

    public LiveData<CustomResponse<?>> observeMessages() {
        return messages;
    }

    /*private QBChatDialog getChatById(String chatId){
        if(chats.getValue() != null && chats.getValue().getBody() != null){
            List<Chat>
            for (QBChatDialog chat :
                    chats.getValue()) {
                if(chat.getDialogId().equals(chatId)) return chat;
            }
        }
        return null;
    }*/

}
