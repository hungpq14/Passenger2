package com.fit.uet.passengerapp.models;

/**
 * Created by phamtruong on 3/11/17.
 */

public class OrderedItem  extends AbstractItem{
    public OrderedItem(String label) {
        super(label);
    }


    @Override
    public int getType() {
        return TYPE_ORDERED;
    }
}
