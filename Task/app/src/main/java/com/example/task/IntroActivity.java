package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.example.task.Model.SubwayModel.Subway;
import com.example.task.Utils.ContentManager;
import com.example.task.room.AppDatabase;
import com.example.task.room.Metro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    protected int splashTime = 2000;
    private Thread splashThread;

    ContentManager contentManager;
    public ArrayList<Subway> Subways = new ArrayList<Subway>();

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        contentManager = new ContentManager();
        db = AppDatabase.getInstance(this);
        getJsonString();
        splashThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int time = 0;
                    while (time < splashTime) {
                        Thread.sleep(100);
                        time += 100;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    stopIntro();
                }

            }

        });

        splashThread.start();
    }

    public void stopIntro() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void getJsonString()
    {
        String json = "";

        try {
            AssetManager assetManager = this.getAssets();
            InputStream is = assetManager.open("SubwayData.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            jsonParsing(json);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }

    public void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray subwayArray = jsonObject.getJSONArray("DATA");

            for(int i=0; i<subwayArray.length(); i++)
            {
                JSONObject subwayObject = subwayArray.getJSONObject(i);

                /*Subway subway = new Subway();

                subway.setAddress(subwayObject.getString("adres"));
                subway.setLine(subwayObject.getString("line"));
                subway.setRdnmadr(subwayObject.getString("rdnmadr"));
                subway.setName(subwayObject.getString("statn_nm"));*/

//                System.out.println("----주소 : " + subway.getAddress() + " //라인 : " + subway.getLine() + " //역 이름 : " + subway.getName());

//                Subways.add(subway);

                Metro metro = new Metro((i+1), subwayObject.getString("statn_nm"), subwayObject.getString("rdnmadr"), subwayObject.getString("adres"),
                        subwayObject.getString("line"));
                db.metroDAO().insertMetro(metro);

            }

//            ContentManager.getInstance().setSubways(Subways);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}