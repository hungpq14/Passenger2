package com.fit.uet.passengerapp.Activity.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;

/**
 * Created by Hungpq on 3/2/17.
 */

public abstract class BaseToolBarActivity extends BaseFontActivity {
    private int layoutId;
    private String toolbarText = "Falcon Cleaner";
    protected TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        setupToolbar();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_vector_back));
        mTitleView = ((TextView) findViewById(R.id.txt_toolbar));
        mTitleView.setText(getToolbarText());
    }

    public void setTitle(String title){
        mTitleView.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract int getLayoutId();

    protected abstract String getToolbarText();
}

