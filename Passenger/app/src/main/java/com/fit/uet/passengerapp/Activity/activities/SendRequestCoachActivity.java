package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.CoachRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SendRequestCoachActivity extends BaseToolBarActivity {

    private EditText edtFrom, edtTo, edtNumSeats, edtTimeMax, edtTimeMin;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDraw();
    }

    private void initDraw() {
        edtFrom = (EditText) findViewById(R.id.edt_arrive_from);
        edtTo = (EditText) findViewById(R.id.edt_arrive_to);
        edtNumSeats = (EditText) findViewById(R.id.edt_ticket_count);
        edtTimeMax = (EditText) findViewById(R.id.edt_time_max);
        edtTimeMin = (EditText) findViewById(R.id.edt_time_min);
        btnPost = (Button) findViewById(R.id.btn_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoachRequest coachRequest = new CoachRequest(edtTo.getText().toString(), edtFrom.getText().toString(),
                        Integer.parseInt(edtNumSeats.getText().toString()), edtTimeMax.getText().toString(),
                        edtTimeMin.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                FirebaseDatabase.getInstance().getReference().child("coach-request").push().setValue(coachRequest);

                Toast.makeText(SendRequestCoachActivity.this, "Yêu cầu đã được đăng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_request_coach;
    }

    @Override
    protected String getToolbarText() {
        return "Gửi yêu cầu đi xe";
    }
}
