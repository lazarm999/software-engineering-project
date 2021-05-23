package com.parovi.zadruga.viewModels;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChatViewModel extends AndroidViewModel {
    private LiveData<QBChatDialog> chat;
    private MutableLiveData<List<QBChatDialog>> chats;
    private MutableLiveData<QBChatMessage> newMessage;
    private MutableLiveData<Boolean> isSent;
    private MutableLiveData<Boolean> isConnected;
    private MutableLiveData<List<QBChatMessage>> messages;
    private QBChatDialogMessageListener newMessageListener = new QBChatDialogMessageListener() {
        @Override
        public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
            newMessage.postValue(qbChatMessage);
        }

        @Override
        public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer senderId) {
            newMessage.postValue(null);
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
    }

    public LiveData<Boolean> observeIsConnected() {
        return isConnected;
    }

    public void connectToChatServer(User u){
        rep.connectToChatServer(isConnected, u);
    }

    public LiveData<List<QBChatDialog>> observeChats(){
        return chats;
    }

    public void getAllChats(){
        rep.getAllChats(chats);
    }
    public MutableLiveData<QBChatMessage> observeNewMessages(){
        return newMessage;
    }
    public void onGlobalMessageReceived(){
        rep.onGlobalMessageReceived(newMessageListener);
    }

    public void sendMessage(QBChatMessage message){
        QBChatDialog chat;
        if((chat = getChatById(message.getDialogId())) != null)
            rep.sendMessage(isSent, chat, message);
    }

    public void removeGlobalMessageListener(){
        rep.removeGlobalMessageReceivedListener(newMessageListener);
    }

    public LiveData<List<QBChatMessage>> observeMessages() {
        return messages;
    }

    public void getMessages(String chatId) {
        QBChatDialog chat;
        if((chat = getChatById(chatId)) != null)
            rep.getMessages(messages, chat);
    }

    private QBChatDialog getChatById(String chatId){
        if(chats.getValue() != null){
            for (QBChatDialog chat :
                    chats.getValue()) {
                if(chat.getDialogId().equals(chatId)) return chat;
            }
        }
        return null;
    }

}
