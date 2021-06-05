package com.parovi.zadruga.viewModels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parovi.zadruga.data.Chat;
import com.parovi.zadruga.data.ChatResume;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ChatsViewModel extends ViewModel {
    private MutableLiveData<List<ChatResume>> chatList;
    private MutableLiveData<Chat> chatInFocus;

    public LiveData<List<ChatResume>> getChatList() {
        if (chatList == null) {
            chatList = new MutableLiveData<List<ChatResume>>();
            loadChatList();
        }
        return chatList;
    }

    public LiveData<Chat> getChatInFocus() {
        if (chatInFocus == null) {
            chatInFocus = new MutableLiveData<Chat>();
            loadChatInFocus();
        }
        return chatInFocus;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadChatList() {
        List<ChatResume> chatResumes = new LinkedList<ChatResume>();
        for (int i = 0; i < 5; i++) {
            chatResumes.add(0, new ChatResume((long) i,
                    "Stojke " + Integer.toString(i),
                    "Magdonac, ce mi zavrsis jedan poslic?",
                    LocalDateTime.now()));
            if (i != 3)
                chatResumes.get(0).setSeen(true);
        }
        chatList.setValue(chatResumes);
    }
    private void loadChatInFocus() {
        chatInFocus.setValue(new Chat("Poslic neki"));
    }
}
