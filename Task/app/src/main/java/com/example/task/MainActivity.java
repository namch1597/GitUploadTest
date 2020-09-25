package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    private static final int MSG_PERMISSIONS_SUBWAY = 1001;
    private static final int MSG_PERMISSIONS_WAETHER = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btTop.setOnClickListener(this);
        binding.btBottom.setOnClickListener(this);
        binding.btLeft.setOnClickListener(this);
        binding.btRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_top:

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MSG_PERMISSIONS_WAETHER);
                    } else {
                        Intent intent = new Intent(this,WeatherActivity.class);
                        startActivity(intent);
                    }
                }*/

                break;


            case R.id.bt_bottom:
                break;


            case R.id.bt_left:

                break;


            case R.id.bt_right:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MSG_PERMISSIONS_SUBWAY);
                    } else {
                        Intent intent = new Intent(this,MapActivity.class);
                        startActivity(intent);
                    }
                }

                break;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MSG_PERMISSIONS_SUBWAY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted
                    Intent intent = new Intent(this,MapActivity.class);
                    startActivity(intent);
                } else {
                    // permission denied
                    Toast.makeText(MainActivity.this, "위치권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case MSG_PERMISSIONS_WAETHER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted
                    Intent intent = new Intent(this,WeatherActivity.class);
                    startActivity(intent);
                } else {
                    // permission denied
                    Toast.makeText(MainActivity.this, "위치권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }




}