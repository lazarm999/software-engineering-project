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
    private Chat activeChat;
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
            if (qbChatMessage.getSenderId() != Utility.getUserQbId(App.getAppContext()) &&
                    activeChat != null && activeChat.getQbChat().getDialogId().equals(dialogId))
                rep.updateMessages(messages, qbChatMessage);
            rep.updateChat(chats, dialogId);
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
    public void setActiveChat(Chat chat) {
        activeChat = chat;
    }

    public MutableLiveData<CustomResponse<?>> observeChats(){
        return chats;
    }

    public MutableLiveData<CustomResponse<?>> observeMembers() {
        return chatMembers;
    }

    public MutableLiveData<QBChatMessage> observeNewMessages(){
        return newMessage;
    }

    public void addOnGlobalMessageReceived(){
        rep.addOnMessageReceivedGlobal(newMessageListener);
    }

    public void sendMessage(String message){
        rep.sendMessage(isSent, messages, chats, activeChat.getQbChat(), message);
    }

    public void removeGlobalMessageListener(){
        rep.removeGlobalMessageReceivedListener(newMessageListener);
    }

    public void loadAllChats(){
        rep.getAllChats(chats);
    }

    public void loadChatMembers(){
        rep.getChatMembers(chatMembers, activeChat.getQbChat());
    }

    public void loadMessages(int offset) {
        rep.getMessages(messages, activeChat.getQbChat(), offset, false);
    }

    public void clearMessages() {
        activeChat = null;
        messages.getValue().setBody(null);
    }

    public void loadAd(){
        rep.getAdByChatId(ad, activeChat.getQbChat().getDialogId());
    }

    public MutableLiveData<CustomResponse<?>> observeAd() {
        return ad;
    }

    public LiveData<CustomResponse<?>> observeMessages() {
        return messages;
    }

    public void isActiveChatPrivate() {
        return;
    }

    public Chat getActiveChat() {
        return activeChat;
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
