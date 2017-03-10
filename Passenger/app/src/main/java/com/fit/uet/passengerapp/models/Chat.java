package com.fit.uet.passengerapp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Bien-kun on 09/03/2017.
 */

public class Chat {
    private final List<Message> messages;

    public Chat(List<Message> messages) {
        this.messages = messages;
    }

    public int size() {
        return messages.size();
    }

    public Message get(int position) {
        return messages.get(position);
    }

    public boolean addMessage(Message message) {
        boolean canAdd = !messages.contains(message);
        if (canAdd) {
            messages.add(message);
        }
        return canAdd;
    }

    public Chat sortByDate() {
        List<Message> sortedList = new ArrayList<>(messages);
        Collections.sort(sortedList, byDate());
        return new Chat(sortedList);
    }

    private Comparator<? super Message> byDate() {
        return new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                long time1 = Long.parseLong(o1.getTimestamp().replace("/", ""));
                long time2 = Long.parseLong(o2.getTimestamp().replace("/", ""));
                return time1 < time2 ? -1 : time1 == time2 ? 0 : 1;
            }
        };
    }
}
