package com.fit.uet.passengerapp.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class FABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public FABBehavior() {
    }

    public FABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {

        if(dependency instanceof  AppBarLayout){
            if(ViewCompat.getTranslationY(dependency) > 0){
                child.show();
            }else {
                child.hide();
            }

        }

        return false;
    }
}
