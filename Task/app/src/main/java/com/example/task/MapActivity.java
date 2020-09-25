package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.task.Model.Subway;
import com.example.task.Utils.ContentManager;
import com.example.task.Utils.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback {

    GpsTracker gpsTracker;
    double latitude = 0.0f;
    double longitude = 0.0f;
    LatLng latLng;

    private GoogleMap mMap;
    ArrayList<Subway>subways = new ArrayList<Subway>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        subways = ContentManager.getInstance().getSubways();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude(); // 위도
        longitude = gpsTracker.getLongitude(); //경도
        if (latitude == 0.0f || longitude == 0.0f) {
            latitude = 37.413294;
            longitude = 127.269311;
            Toast.makeText(this, "서비스 사용불가 재진입해주세요.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String juso = gpsTracker.getCurrentAddress(latitude,longitude);

        System.out.println("----위도 : " + latitude + "경도 : " + longitude + "주소 : " + juso);

        mMap = googleMap;

        LatLng NOW = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(NOW);
        markerOptions.title("현재위치");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NOW, 15));

    }

}