package com.fit.uet.passengerapp.models;

/**
 * Created by Bien-kun on 09/03/2017.
 */

public class Conversation {
    private String key;
    private User user;
    private Message message;
    private boolean unread;


    public Conversation(String key) {
        this.key = key;
    }

    public Conversation(User user, Message message) {
        this.user = user;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || !(obj instanceof Conversation)){
            return false;
        }
        Conversation other = (Conversation) obj;
        return user != null && user.getUid().equals(other.getUser().getUid());
    }
}
