package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCenter;
    Button btTop;
    Button btBottom;
    Button btLeft;
    Button btRight;

    private static final int MSG_PERMISSIONS_REQ = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCenter = findViewById(R.id.tv_center);
        btTop = findViewById(R.id.bt_top);
        btBottom = findViewById(R.id.bt_bottom);
        btLeft = findViewById(R.id.bt_left);
        btRight = findViewById(R.id.bt_right);

        btTop.setOnClickListener(this);
        btBottom.setOnClickListener(this);
        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_top:
                break;


            case R.id.bt_bottom:
                break;


            case R.id.bt_left:
                break;


            case R.id.bt_right:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MSG_PERMISSIONS_REQ);
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
            case MSG_PERMISSIONS_REQ: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted
                    Intent intent = new Intent(this,MapActivity.class);
                    startActivity(intent);
                } else {
                    // permission denied
                    Toast.makeText(MainActivity.this, "위치권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }




}