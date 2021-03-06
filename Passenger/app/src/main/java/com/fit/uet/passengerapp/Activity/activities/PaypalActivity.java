package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.PlaceAutoCompleteAdapter;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.fit.uet.passengerapp.models.Ticket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaypalActivity extends BaseToolBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public String TAG = "PaypalActivity";
    public static final int TYPE_SHIP = 0;
    public static final int TYPE_PAYPAL = 1;
    private CardView cardShip, cardPaypal;
    private Button btnPay;

    private PlaceAutoCompleteAdapter mAdapter;
    AutoCompleteTextView autoCompleteText;
    protected GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_HANOI = new LatLngBounds(new LatLng(21, 105),
            new LatLng(31, 100));

    private Ticket ticket;
    private String seatState;
    private DatabaseReference databaseReference;
    private DatabaseReference scheduleDatabaseReference;
    private DatabaseReference ticketDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDraw();
    }

    private void initDraw() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        scheduleDatabaseReference = databaseReference.child(CoachSchedule.CHILD_COACH_SCHEDULE);
        ticketDatabaseReference = databaseReference.child(Ticket.CHILD_TICKET);


        cardPaypal = (CardView) findViewById(R.id.layout_paypal);
        cardShip = (CardView) findViewById(R.id.layout_pay_with_ship);
        btnPay = (Button) findViewById(R.id.btn_pay_finish);
        autoCompleteText = (AutoCompleteTextView) findViewById(R.id.atv_place_ship);
        initAutoComplete();

        int type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            cardPaypal.setVisibility(View.GONE);
            cardShip.setVisibility(View.VISIBLE);
        } else {
            cardPaypal.setVisibility(View.VISIBLE);
            cardShip.setVisibility(View.GONE);
        }

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket = (Ticket) getIntent().getSerializableExtra("ticket");
                seatState = getIntent().getStringExtra("seatState");

                if (ticket != null && seatState != null) {
                    ticketDatabaseReference.push().setValue(ticket);
                    scheduleDatabaseReference.child(getIntent().getStringExtra(Intent.EXTRA_TEXT)).child("seatState").setValue(seatState);
                }

                Intent intent = new Intent(PaypalActivity.this, TicketClientActivity.class);
                intent.putExtra("ticket", ticket);
                startActivity(intent);
                PaypalActivity.this.finish();
            }
        });

    }

    public void initAutoComplete() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_HANOI, null);

        autoCompleteText.setAdapter(mAdapter);

        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(TAG, "Autocomplete item selected: " + item.description);

                /*
                Issue a request to the Places Geo Data API to retrieve a Place object with additional
                details about the place.
                */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                    }
                });

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paypal;
    }

    @Override
    protected String getToolbarText() {
        return "Thanh toán vé";
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
