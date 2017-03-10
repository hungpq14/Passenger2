package com.fit.uet.passengerapp.models;

/**
 * Created by phamtruong on 3/10/17.
 */

public class EmptyItem extends AbstractItem {

    public EmptyItem(String label) {
        super(label);
    }


    @Override
    public int getType() {
        return TYPE_EMPTY;
    }

}