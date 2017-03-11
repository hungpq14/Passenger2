package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForceInputPhoneNumActivity extends AppCompatActivity {
    public static final String TAG = "ForceInputPhoneNum";
    private EditText edtPhoneNum;
    private Button btnOk;
    DatabaseReference myRef;
    User currentUser;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_input_mobiphone);

        initDraw();
    }

    private void initDraw() {
        edtPhoneNum = (EditText) findViewById(R.id.edt_input_phone_num);
        btnOk = (Button) findViewById(R.id.btn_ok_phone_num);

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//        myRef.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.child("phoneNum").exists()) {
//                    startActivity(new Intent(ForceInputPhoneNumActivity.this, MainUIActivity.class));
//                    ForceInputPhoneNumActivity.this.finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNum = edtPhoneNum.getText().toString();

                if (!phoneNum.trim().equals("")) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    currentUser = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), phoneNum, 0);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    myRef = database.getReference();
                    myRef.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                addUserToDB();
                            } else {
                                if (!dataSnapshot.child("phoneNum").exists()) {
                                    Log.d(TAG, "set value phone num in ref");
                                    dataSnapshot.child("phoneNum").getRef().setValue(phoneNum);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    Log.d(TAG, "push new user to db");
//                    myRef.child("users").child(firebaseUser.getUid()).setValue(currentUser);

                    startActivity(new Intent(ForceInputPhoneNumActivity.this, MainUIActivity.class));
                    ForceInputPhoneNumActivity.this.finish();
                }
            }
        });
    }

    public void addUserToDB() {
        Log.d(TAG, "push new user to db");
        DatabaseReference db = myRef.child("users").child(firebaseUser.getUid());
        db.setValue(currentUser);
    }
}
