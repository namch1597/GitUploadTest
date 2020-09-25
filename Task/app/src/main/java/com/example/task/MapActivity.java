package com.example.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.task.Model.DirectionResponses;
import com.example.task.Model.Subway;
import com.example.task.Network.APIClient;
import com.example.task.Network.APIInterface;
import com.example.task.Utils.ContentManager;
import com.example.task.Utils.GpsTracker;
import com.example.task.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback,View.OnClickListener {

    ActivityMapBinding binding;

    GpsTracker gpsTracker;
    double latitude = 0.0f;
    double longitude = 0.0f;
    LatLng latLng;

    public static Context mContext;

    private GoogleMap mMap;
    ArrayList<Subway>subways = new ArrayList<Subway>();
    ArrayList<String>subwayNames = new ArrayList<String>();

    ArrayAdapter spinnerLeftAdapter;
    ArrayAdapter spinnerRightAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        mContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        subways = ContentManager.getInstance().getSubways();

        binding.btSearch.setOnClickListener(this);

        FirstViewSetting();

    }

    public void FirstViewSetting() {
        for (int i=0; i<subways.size(); i++) {
            subwayNames.add(subways.get(i).getName());
        }
        spinnerLeftAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, subwayNames);
        binding.spLeft.setAdapter(spinnerLeftAdapter);

        spinnerRightAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, subwayNames);
        binding.spRight.setAdapter(spinnerRightAdapter);

    }

    public String getRealJuso(String subwayName) {
        String juso = "";


        for (int i=0; i<subways.size(); i++) {
            if (subways.get(i).getName().equals(subwayName)) {
                juso = subways.get(i).getAddress();
            }
        }

        return juso;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_search:

                Address leftText = getCurrentAddress(0.0f,0.0f,getRealJuso(binding.spLeft.getSelectedItem().toString()));
                Address rightText = getCurrentAddress(0.0f,0.0f,getRealJuso(binding.spRight.getSelectedItem().toString()));

                String from = String.valueOf(leftText.getLatitude()) + "," + String.valueOf(leftText.getLongitude());
                String to = String.valueOf(rightText.getLatitude()) + "," + String.valueOf(rightText.getLongitude());

                APIInterface apiServices = APIClient.apiServices(this);
                apiServices.getDirection(from, to, "transit","AIzaSyA9lCWN--EpZshKkp2hrfsE-9Sr8GZyIQc")
                        .enqueue(new Callback<DirectionResponses>() {
                            @Override
                            public void onResponse(@NonNull Call<DirectionResponses> call, @NonNull Response<DirectionResponses> response) {
                                mMap.clear();
                                drawPolyline(response);
                                Log.d("bisa dong oke", response.message());

                                LatLng SEARCHSTART = new LatLng(leftText.getLatitude(), leftText.getLongitude());

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SEARCHSTART, 15));

                            }

                            @Override
                            public void onFailure(@NonNull Call<DirectionResponses> call, @NonNull Throwable t) {
                                Log.e("anjir error", t.getLocalizedMessage());
                            }
                        });
                break;

        }

    }

    private void drawPolyline(@NonNull Response<DirectionResponses> response) {
        if (response.body() != null) {
            String shape = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
            PolylineOptions polyline = new PolylineOptions()
                    .addAll(PolyUtil.decode(shape))
                    .width(8f)
                    .color(Color.RED);
            mMap.addPolyline(polyline);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        gpsTracker = new GpsTracker(mContext);

        latitude = gpsTracker.getLatitude(); // 위도
        longitude = gpsTracker.getLongitude(); //경도
        if (latitude == 0.0f || longitude == 0.0f) {
            latitude = 37.413294;
            longitude = 127.269311;
            Toast.makeText(this, "서비스 사용불가 재진입해주세요.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Address address = getCurrentAddress(latitude,longitude,"");

        mMap = googleMap;

        LatLng NOW = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(NOW);
        markerOptions.title("현재위치");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NOW, 15));

    }

    public Address getCurrentAddress( double latitude, double longitude, String juso) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            // 0.0f 주소로 변환
            if (latitude == 0.0f) {
                addresses = geocoder.getFromLocationName( juso, 3);
            } else {
                addresses = geocoder.getFromLocation( latitude, longitude, 3);
            }
        } catch (IOException ioException) {
            //네트워크 문제

        } catch (IllegalArgumentException illegalArgumentException) {

        }
        if (addresses == null || addresses.size() == 0) {

        }
        Address address = addresses.get(0);
        return address;
        //여기부터는 GPS 활성화를 위한 메소드들
    }


}