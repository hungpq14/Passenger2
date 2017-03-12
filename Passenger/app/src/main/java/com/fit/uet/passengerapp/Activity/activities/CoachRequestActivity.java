package com.fit.uet.passengerapp.Activity.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseFontActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.PlaceAutoCompleteAdapter;
import com.fit.uet.passengerapp.models.City;
import com.fit.uet.passengerapp.models.CoachRequest;
import com.fit.uet.passengerapp.ui.DetailMarkerDialog;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.fit.uet.passengerapp.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CoachRequestActivity extends BaseFontActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "CoachRequestActivity";
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    protected GoogleMap map;
    AutoCompleteTextView autoCompleteText;
    private static final LatLngBounds BOUNDS_HANOI = new LatLngBounds(new LatLng(21, 105),
            new LatLng(31, 100));
    MarkerOptions marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_show_request_map);

        initDraw();
    }

    private void initDraw() {
        final AutoCompleteTextView autoCompleteText = (AutoCompleteTextView) findViewById(R.id.auto_complete_text);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(this);
        mGoogleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }


        if (map == null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;

                    map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition position) {
                            LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                            mAdapter.setBounds(bounds);
                        }
                    });
                    LatLng latLng = new LatLng(21.027764, 105.834160);

                    Log.d(TAG, "Animated camera map");
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 39.0f), 2000, null);

                    addMarker();
                }
            });
        }

        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_HANOI, null);

        autoCompleteText.setAdapter(mAdapter);

        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(TAG, "Autocomplete item selected: " + item.description);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                        Log.d(TAG, place.getLatLng().longitude + " " + place.getLatLng().latitude);
                        LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30.0f), 3000, null);

                        hideKeyboard();
                    }
                });

            }
        });
    }

    public void hideKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        View view = CoachRequestActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void addMarker() {
//        BitmapDescriptor bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.);
//        MarkerOptions marker = new MarkerOptions().position(new LatLng(20.955835, 105.756366)).icon(getTextMarker("Nam Dinh"));
//        map.addMarker(marker).setTag(new CoachRequest(4));
//        marker = new MarkerOptions().position(new LatLng(21.036237, 105.790583)).icon(getTextMarker("Nam Dinh"));
//        map.addMarker(marker);
//        marker = new MarkerOptions().position(new LatLng(21.010040, 105.533215)).icon(getTextMarker("Thanh Hóa"));
//        map.addMarker(marker);
//        marker = new MarkerOptions().position(new LatLng(20.583520, 105.922990)).icon(getTextMarker("Yên Bái"));
//        map.addMarker(marker);
//        marker = new MarkerOptions().position(new LatLng(20.852571, 106.016997)).icon(getTextMarker("Thái Bình"));
//        map.addMarker(marker);

//        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Log.d(TAG, marker.getSnippet());
//                CoachRequest coachRequest = (CoachRequest) marker.getTag();
//                Log.d(TAG, coachRequest.numTicket + "");
//                return false;
//            }
//        });

        final ArrayList<CoachRequest> coachRequests = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("coach-request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    coachRequests.add(snapshot.getValue(CoachRequest.class));
                }
                for (CoachRequest coachRequest : coachRequests) {
                    //todo remove time max here
                    if (DateTimeUtils.getMillisFromString(coachRequest.timeMaxToStart) < System.currentTimeMillis()) {
                        Log.d(TAG, coachRequest.timeMaxToStart + " "
                                + DateTimeUtils.getMillisFromString(coachRequest.timeMaxToStart) + " "
                                + System.currentTimeMillis());
                        continue;
                    }
                    new DataLongOperationAsynchTask().execute(coachRequest);

                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            CoachRequest coachRequest = (CoachRequest) marker.getTag();
                            Log.d(TAG, coachRequest.uid + "");

                            DetailMarkerDialog dialog = new DetailMarkerDialog(CoachRequestActivity.this, coachRequest);
                            dialog.show();

                            return false;
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public BitmapDescriptor getTextMarker(String text) {

        Paint paint = new Paint();
        /* Set text size, color etc. as needed */
        paint.setTextSize(30);

        int width = (int) paint.measureText(text);
        int height = (int) paint.getTextSize();

        paint.setTextSize(24);
        paint.setColor(getApplicationContext().getResources().getColor(R.color.text_primary_default_light));
        paint.setTextAlign(Paint.Align.CENTER);
        // Create a transparent bitmap as big as you need
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        // During development the following helps to see the full
        // drawing area:
        canvas.drawColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        // Start drawing into the canvas
        canvas.translate(width / 2f, height);
        canvas.drawText(text, 0, 0, paint);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);
        return icon;
    }

    //get latitude and longitude from string
    private class DataLongOperationAsynchTask extends AsyncTask<CoachRequest, Void, String[]> {
        CoachRequest coachRequest;

        @Override
        protected String[] doInBackground(CoachRequest... params) {
            coachRequest = params[0];
            String response;
            try {
                String jsonLocation = coachRequest.arriveFrom.replaceAll("\\s", "%20");
                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=" + jsonLocation + "&sensor=false");
//                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=hanoi&sensor=false");
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                LatLng locationLatLng = new LatLng(latitude, longitude);

//                map.addMarker(new MarkerOptions().position(locationLatLng).title(coachRequest.arriveTo));
                marker = new MarkerOptions().position(locationLatLng).icon(getTextMarker(coachRequest.arriveTo));
                map.addMarker(marker).setTag(coachRequest);

                Log.d("add latitude", "" + latitude);
                Log.d("add longitude", "" + longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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
