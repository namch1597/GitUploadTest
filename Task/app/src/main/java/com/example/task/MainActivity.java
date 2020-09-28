package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.task.Utils.BackPressCloseHandler;
import com.example.task.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    ActivityMainBinding binding;

    private static final int MSG_PERMISSIONS_SUBWAY = 1001;
    private static final int MSG_PERMISSIONS_WAETHER = 1002;

    float oldXvalue;
    float oldYvalue;

    float parentWidth;

    int centerXvalue;
    int centerYvalue;
    Vibrator vibrator;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        vibrator = (Vibrator)getSystemService(this.VIBRATOR_SERVICE);

        binding.btTop.setOnClickListener(this);
        binding.btBottom.setOnClickListener(this);
        binding.btLeft.setOnClickListener(this);
        binding.btRight.setOnClickListener(this);
        binding.llChildCenter.setOnTouchListener(this);

        backPressCloseHandler = new BackPressCloseHandler(this);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.ll_center);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                } else {
                    layout.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
                parentWidth = layout.getWidth() / 4;
                centerXvalue  = layout.getWidth();
                centerYvalue = layout.getHeight();

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_top:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MSG_PERMISSIONS_WAETHER);
                    } else {
                        Intent weatherIntent = new Intent(this,WeatherActivity.class);
                        startActivity(weatherIntent);
                    }
                }

                break;


            case R.id.bt_bottom:
                Intent interactionIntent = new Intent(this,InteractionActivity.class);
                startActivity(interactionIntent);
                break;


            case R.id.bt_left:
                Toast.makeText(MainActivity.this, "준비중입니다.", Toast.LENGTH_SHORT).show();
                break;


            case R.id.bt_right:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MSG_PERMISSIONS_SUBWAY);
                    } else {
                        Intent mapIntent = new Intent(this,MapActivity.class);
                        startActivity(mapIntent);
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int parentWidth = ((ViewGroup)v.getParent()).getWidth();    // 부모 View 의 Width
        int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height
        //1401 1161

        Animation animation
                = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.interpolator);

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            // 뷰 누름
            oldXvalue = event.getX();
            oldYvalue = event.getY();

            vibrator.vibrate(30);

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            // 뷰 이동 중
            v.setX(v.getX() + (event.getX()) - (v.getWidth()/2));
            v.setY(v.getY() + (event.getY()) - (v.getHeight()/2));

        }else if(event.getAction() == MotionEvent.ACTION_UP){
            // 뷰에서 손을 뗌

            if ((v.getX() >= (((centerXvalue / 2) - (v.getWidth()/2)) ) && v.getY() <= (40)))  {
                //top
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
            else if (v.getX() <= (50) && v.getY() <= ((centerYvalue / 2) - 50)) {
               //left
                Toast.makeText(MainActivity.this, "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
            else if (v.getX() >= (centerXvalue  - v.getWidth() - 50) && v.getY() <=((centerYvalue / 2) - (v.getHeight() / 2))) {
                //right
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
            else if (v.getX() <= ((centerXvalue / 2) - (v.getWidth()/2)) && v.getY() >= centerYvalue - v.getHeight() - 50) {
                //bottom
                Intent intent = new Intent(MainActivity.this, InteractionActivity.class);
                startActivity(intent);
            }
            /*System.out.println("-------top : " + (((centerXvalue / 2) - (v.getWidth()/2))) + "getX : " + v.getX()
             + "getY : " + v.getY());

            System.out.println("-------left : getX : " + v.getX() + " y : " + ((centerYvalue / 2) - 50) + "getY : " + v.getY());


            System.out.println("-------right : " + ((centerXvalue  - v.getWidth() - 50)) + "getX : " + v.getX()
                    + "getY : " + v.getY() + " y : " + ((centerYvalue / 2) - (v.getHeight() / 2)));

            System.out.println("------bottom : " + v.getX() +" ?:"  + ((centerXvalue / 2) - (v.getWidth()/2)) + "y :"
                    + v.getY() + " yyy: " + (centerYvalue - v.getHeight()));
*/
            v.setX((centerXvalue / 2) - (v.getWidth()/2));
            v.setY((centerYvalue / 2) - (v.getHeight()/2));

        }

        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}