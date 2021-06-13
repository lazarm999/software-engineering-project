package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private QBChatDialog activeChat;
    private MutableLiveData<CustomResponse<?>> chats;
    private MutableLiveData<QBChatMessage> newMessage;
    private MutableLiveData<CustomResponse<?>> isSent;
    private MutableLiveData<CustomResponse<?>> isConnected;
    private MutableLiveData<CustomResponse<?>> messages;
    private MutableLiveData<CustomResponse<?>> chatMembers;
    private MutableLiveData<CustomResponse<?>> ad;
    private int adId = 5;
    private QBChatDialogMessageListener newMessageListener = new QBChatDialogMessageListener() {
        @Override
        public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
            if (activeChat != null && activeChat.getDialogId().equals(dialogId))
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
        chatMembers = new MutableLiveData<>();
        ad = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> observeIsConnected() {
        return isConnected;
    }

    public void connectToChatServer(){
        rep.connectToChatServer(isConnected);
    }
    public void setActiveChat(QBChatDialog chatDialog) {
        activeChat = chatDialog;
    }

    public MutableLiveData<CustomResponse<?>> getChats(){
        return chats;
    }

    public MutableLiveData<QBChatMessage> getNewMessage(){
        return newMessage;
    }

    public void addOnGlobalMessageReceived(){
        rep.addOnMessageReceivedGlobal(newMessageListener);
    }

    public void sendMessage(String message){
        rep.sendMessage(isSent, activeChat, message);
    }

    public void removeGlobalMessageListener(){
        rep.removeGlobalMessageReceivedListener(newMessageListener);
    }

    public void loadAllChats(){
        rep.getAllChats(chats);
    }

    public void loadChatMembers(){
        rep.getChatMembers(chatMembers, activeChat);
    }

    public void loadMessages(int offset) {
        rep.getMessages(messages, activeChat, offset);
    }

    public void clearMessages() {
        activeChat = null;
        messages.getValue().setBody(null);
    }

    public void getAd(){
        rep.getAdByChatId(ad, activeChat.getDialogId());
    }

    public LiveData<CustomResponse<?>> getMessages() {
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
