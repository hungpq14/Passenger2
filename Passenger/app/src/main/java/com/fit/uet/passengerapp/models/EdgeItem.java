package com.fit.uet.passengerapp.models;

/**
 * Created by phamtruong on 3/10/17.
 */

public class EdgeItem extends AbstractItem {

    public EdgeItem(String label) {
        super(label);
    }



    @Override
    public int getType() {
        return TYPE_EDGE;
    }

}
