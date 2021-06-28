package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.repository.ChatRepository;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

public class ChatViewModel extends ViewModel {
    private static final String QB_CHAT_ID = "QbChatId";

    private SavedStateHandle state;
    private Chat activeChat;
    private String activeQbChatId;
    private QBChatDialog activeQbChat;
    private MutableLiveData<CustomResponse<?>> chats;
    private MutableLiveData<QBChatMessage> newMessage; // TODO: Lazar da pojasni
    private MutableLiveData<CustomResponse<?>> isSent;
    private MutableLiveData<CustomResponse<?>> isConnected;
    private MutableLiveData<CustomResponse<?>> messages;
    private MutableLiveData<CustomResponse<?>> chatMembers;
    private MutableLiveData<CustomResponse<?>> ad;
    private int adId = 32;
    private ChatRepository chatRepository;
    private QBChatDialogMessageListener newMessageListener = new QBChatDialogMessageListener() {
        @Override
        public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
            if (qbChatMessage.getSenderId() != Utility.getLoggedInUserQbId(App.getAppContext()) &&
                    activeQbChat != null && activeQbChat.getDialogId().equals(dialogId))
                chatRepository.updateMessages(messages, qbChatMessage);
            chatRepository.updateChat(chats, dialogId);
        }

        @Override
        public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer senderId) {
            //newMessage.postValue(null);
        }
    };

    public ChatViewModel(SavedStateHandle state) {
        this.state = state;

        chatRepository = new ChatRepository();
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
        chatRepository.connectToChatServer(isConnected);
    }

    public void setActiveChat(Chat chat) {
        activeChat = chat;
    }

    public void setActiveQbChat(QBChatDialog chat) {
        activeQbChat = chat;
        activeQbChatId = chat.getDialogId();
        // save active chat
        state.set(QB_CHAT_ID, activeQbChatId);
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
        chatRepository.addOnMessageReceivedGlobal(newMessageListener);
    }

    public void sendMessage(String message){
        chatRepository.sendMessage(isSent, messages, chats, activeQbChat, message);
    }

    public void removeGlobalMessageListener(){
        chatRepository.removeGlobalMessageReceivedListener(newMessageListener);
    }

    private void loadSavedState() {
        activeQbChatId = state.get(QB_CHAT_ID);
        // TODO: dovuci aktivni QbDialog
    }

    public void loadAllChats(){
        chatRepository.getAllChats(chats);
    }

    public void loadChatMembers(){
        chatRepository.getChatMembers(chatMembers, activeQbChat);
    }

    public void loadMessages(int offset) {
        chatRepository.getMessages(messages, activeQbChat, offset, false);
    }

    public void leaveChat() {
        activeChat = null;
        messages.getValue().setBody(null);
    }

    public void loadAd(){
        chatRepository.getAdByChatId(ad, activeQbChat.getDialogId());
    }

    public int getAdId() {
        return activeChat.getFkAdId();
    }

    public MutableLiveData<CustomResponse<?>> observeAd() {
        return ad;
    }

    public LiveData<CustomResponse<?>> observeMessages() {
        return messages;
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
