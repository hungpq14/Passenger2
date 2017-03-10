package com.fit.uet.passengerapp.adapter;

import com.google.firebase.database.DatabaseError;


public interface ChangeEventListener {
    /**
     * The type of event received when a child has been updated.
     */
    enum EventType {
        ADDED,
        CHANGED,
        REMOVED,
        MOVED
    }

    void onChildChanged(EventType type, int index, int oldIndex);

    /**
     * This method will be triggered each time updates from the database have been completely processed.
     * So the first time this method is called, the initial data has been loaded - including the case
     * when no data at all is available. Each next time the method is called, a complete update (potentially
     * consisting of updates to multiple child items) has been completed.
     * <p>
     * You would typically override this method to hide a loading indicator (after the initial load) or
     * to complete a batch update to a UI element.
     */
    void onDataChanged();

    /**
     * This method will be triggered in the event that this listener either failed at the server,
     * or is removed as a result of the security and Firebase Database rules.
     *
     * @param error A description of the error that occurred
     */
    void onCancelled(DatabaseError error);
}
