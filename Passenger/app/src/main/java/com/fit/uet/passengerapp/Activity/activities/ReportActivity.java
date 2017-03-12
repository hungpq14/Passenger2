package com.fit.uet.passengerapp.Activity.activities;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;

/**
 * Created by Bien-kun on 12/03/2017.
 */

public class ReportActivity extends BaseToolBarActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    protected String getToolbarText() {
        return getString(R.string.activity_report_title);
    }

}
