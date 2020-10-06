package com.example.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.task.Model.SubwayModel.DirectionResponses;
import com.example.task.Model.SubwayModel.Subway;
import com.example.task.Network.APIClient;
import com.example.task.Network.APIInterface;
import com.example.task.Utils.ContentManager;
import com.example.task.Utils.GpsTracker;
import com.example.task.databinding.ActivityMapBinding;
import com.example.task.room.AppDatabase;
import com.example.task.room.Metro;
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

    public static Context mContext;

    private GoogleMap mMap;
    ArrayList<Subway>subways = new ArrayList<Subway>();
    ArrayList<String>subwayNames = new ArrayList<String>();

    List<Metro>metros;
    ArrayList<String>metroNames = new ArrayList<String>();

    ArrayAdapter spinnerLeftAdapter;
    ArrayAdapter spinnerRightAdapter;

    String realJuso;
    LatLng NOW;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        mContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = AppDatabase.getInstance(this);
        metros = db.metroDAO().getAll();

        //json singleton 방식
//        subways = ContentManager.getInstance().getSubways();

        binding.btSearch.setOnClickListener(this);
        binding.tvNowLocation.setOnClickListener(this);

        FirstViewSetting();

    }

    public void FirstViewSetting() {
        Toolbar toolbar = (Toolbar) binding.tbToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //json singleton 방식
        /*for (int i=0; i<subways.size(); i++) {
            subwayNames.add(subways.get(i).getName());
        }
        spinnerLeftAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, subwayNames);
        binding.spLeft.setAdapter(spinnerLeftAdapter);

        spinnerRightAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, subwayNames);
        binding.spRight.setAdapter(spinnerRightAdapter);*/

        for (int i=0; i<metros.size(); i++) {
            metroNames.add(metros.get(i).getName());
        }

        spinnerLeftAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, metroNames);
        binding.spLeft.setAdapter(spinnerLeftAdapter);

        spinnerRightAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, metroNames);
        binding.spRight.setAdapter(spinnerRightAdapter);

    }

    public String getRealJuso(String subwayName) {
        String juso = "";

        //json singleton 방식
        /*for int i=0; i<subways.size(); i++) {
            if (subways.get(i).getName().equals(subwayName)) {
                juso = subways.get(i).getRdnmadr();
            }
        }*/

        Metro metro = db.metroDAO().getDetailPath(subwayName);
        juso = metro.getRealPath();

        System.out.println("---juso : " + juso);

        return juso;
    }

    private void drawPolyline(@NonNull Response<DirectionResponses> response) {
        if (response.body() != null) {
            if (response.body().getRoutes().size() == 0 || response.body().getRoutes().get(0).getOverviewPolyline().getPoints() == null) {
                mMap.clear();

                addMyLocationMarker();

                return;
            }
            String shape = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
            PolylineOptions polyline = new PolylineOptions()
                    .addAll(PolyUtil.decode(shape))
                    .width(15f)
                    .color(Color.RED);
            mMap.addPolyline(polyline);

            binding.tvLeft.setVisibility(View.VISIBLE);
            binding.tvRight.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        binding.pbCenter.setVisibility(View.VISIBLE);

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

        mMap = googleMap;

        NOW = new LatLng(latitude, longitude);

        addMarker(NOW,"현재위치");

        realJuso = getCurrentAddress(latitude,longitude,"").getAddressLine(0);
        binding.tvNowLocation.setText(Html.fromHtml("<u>" + realJuso + "</u>"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NOW, 15));

        binding.pbCenter.setVisibility(View.INVISIBLE);

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
            return null;
        }
        Address address = addresses.get(0);
        return address;
        //여기부터는 GPS 활성화를 위한 메소드들
    }

    public void addMarker(LatLng latLng, String title) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        mMap.addMarker(markerOptions);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_search:

                binding.pbCenter.setVisibility(View.VISIBLE);

                Address leftText = getCurrentAddress(0.0f,0.0f,getRealJuso(binding.spLeft.getSelectedItem().toString()));
                if (leftText == null) {
                    addMyLocationMarker();
                    Toast.makeText(MapActivity.this, "위치찾기 실패 현재위치로 이동합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng leftLatLng = new LatLng(leftText.getLatitude(),leftText.getLongitude());

                Address rightText = getCurrentAddress(0.0f,0.0f,getRealJuso(binding.spRight.getSelectedItem().toString()));
                if (rightText == null) {
                    addMyLocationMarker();
                    Toast.makeText(MapActivity.this, "위치찾기 실패 현재위치로 이동합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng rightLatLng = new LatLng(rightText.getLatitude(),rightText.getLongitude());


                binding.tvLeft.setText(binding.spLeft.getSelectedItem().toString()+" 위도,경도 : " + Double.toString(leftText.getLatitude()).substring(0,7)
                        + "," + Double.toString(leftText.getLongitude()).substring(0,8));
                binding.tvRight.setText(binding.spRight.getSelectedItem().toString()+" 위도,경도 : " + Double.toString(rightText.getLatitude()).substring(0,7)
                        + "," + Double.toString(rightText.getLongitude()).substring(0,8));


                String from = String.valueOf(leftText.getLatitude()) + "," + String.valueOf(leftText.getLongitude());
                String to = String.valueOf(rightText.getLatitude()) + "," + String.valueOf(rightText.getLongitude());

                APIInterface subwayServices = APIClient.subwayServices(this);
                subwayServices.getDirection(from, to, "transit","AIzaSyA9lCWN--EpZshKkp2hrfsE-9Sr8GZyIQc")
                        .enqueue(new Callback<DirectionResponses>() {
                            @Override
                            public void onResponse(@NonNull Call<DirectionResponses> call, @NonNull Response<DirectionResponses> response) {
                                mMap.clear();
                                if (response.body() == null) {

                                    addMyLocationMarker();

                                    Toast.makeText(MapActivity.this, "위치찾기 실패 현재위치로 이동합니다.", Toast.LENGTH_SHORT).show();

                                    binding.tvLeft.setVisibility(View.GONE);
                                    binding.tvRight.setVisibility(View.GONE);


                                } else {
                                    addMarker(leftLatLng,"출발지점");
                                    addMarker(rightLatLng,"도착지점");

                                    LatLng SEARCHSTART = new LatLng(leftText.getLatitude(), leftText.getLongitude());

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SEARCHSTART, 15));

                                    drawPolyline(response);
                                    Log.d("bisa dong oke", response.message());
                                }


                                binding.pbCenter.setVisibility(View.INVISIBLE);

                            }

                            @Override
                            public void onFailure(@NonNull Call<DirectionResponses> call, @NonNull Throwable t) {
                                Log.e("anjir error", t.getLocalizedMessage());
                                binding.pbCenter.setVisibility(View.INVISIBLE);
                            }
                        });
                break;
            case R.id.tv_now_location:
                binding.tvLeft.setVisibility(View.GONE);
                binding.tvRight.setVisibility(View.GONE);

                binding.pbCenter.setVisibility(View.VISIBLE);

                mMap.clear();

                addMyLocationMarker();

                binding.pbCenter.setVisibility(View.INVISIBLE);

                break;

        }

    }

    public void addMyLocationMarker() {

        //위도 경도 TEXT 들
        binding.tvLeft.setVisibility(View.GONE);
        binding.tvRight.setVisibility(View.GONE);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(NOW);
        markerOptions.title("현재위치");
        mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NOW, 15));
        binding.pbCenter.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}