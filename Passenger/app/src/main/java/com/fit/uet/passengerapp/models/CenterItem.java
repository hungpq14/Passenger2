package com.fit.uet.passengerapp.models;

/**
 * Created by phamtruong on 3/10/17.
 */

public class CenterItem extends AbstractItem {

    public CenterItem(String label) {
        super(label);
    }


    @Override
    public int getType() {
        return TYPE_CENTER;
    }

}
