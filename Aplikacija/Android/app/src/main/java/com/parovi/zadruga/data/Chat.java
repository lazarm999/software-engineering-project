package com.parovi.zadruga.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Chat {
    private String chatTitle;
    private List<Message> messages;
    private List<UserInfoResume> participants;

    public Chat(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public List<Message> getMessages() {
        if (messages == null) {
            messages = new LinkedList<Message>();
            generateMessages();
        }
        return messages;
    }

    public List<UserInfoResume> getParticipants() {
        if (participants == null) {
            participants = new ArrayList<UserInfoResume>();
            loadUsers();
        }
        return participants;
    }

    private void loadUsers() {
        for (int i = 0; i < 3; i++) {
            participants.add(new UserInfoResume(i+1, "Participant" + i, "@username" + i));
        }
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    private void generateMessages() {
        messages.add(0, new Message("Zdravo, ja sam Uros!", false));
        messages.add(0, new Message("Zdravo, ja sam Lazar!", true));
        messages.add(0, new Message("Sta ima novo!", false));
        messages.add(0, new Message("Nema nista!", true));
    }
}
