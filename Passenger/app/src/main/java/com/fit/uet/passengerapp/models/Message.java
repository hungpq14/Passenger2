package com.fit.uet.passengerapp.models;

/**
 * Created by Bien-kun on 09/03/2017.
 */

public class Message {
    private String sender;
    private String destination;
    private String message;
    private String timestamp;

    public Message() {
    }

    public Message(String sender, String destination, String message, String timestamp) {
        this.sender = sender;
        this.destination = destination;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Message)) {
            return false;
        }
        Message msg = (Message) obj;
        return message.equals(msg.getMessage()) && timestamp.equals(msg.timestamp);

    }
}
