package com.fit.uet.passengerapp.Activity.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseFontActivity;
import com.fit.uet.passengerapp.Activity.fragments.FilterFragment;
import com.fit.uet.passengerapp.R;

public class MainUIActivity extends BaseFontActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FilterFragment mFilterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        initDraw();
    }

    private void initDraw() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        initDrawer();

        /**
         * get the main fragment
         */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if (mFilterFragment == null) {
            mFilterFragment = new FilterFragment();
            Log.d("Main", "add fragment main");
            transaction.add(R.id.container, mFilterFragment);
        } else {
            transaction.show(mFilterFragment);
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mFilterFragment != null) {
            transaction.hide(mFilterFragment);
        }
    }

    public void initDrawer() {
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu));
        }

        //set action cho show up negative layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opendrawer, R.string.closedrawer) {
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    int drawerLockMode = drawerLayout.getDrawerLockMode(GravityCompat.START);
                    if (drawerLayout.isDrawerVisible(GravityCompat.START)
                            && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {

                    } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                    return true;
                }
                return false;
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

    }


}
