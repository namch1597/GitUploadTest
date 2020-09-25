package com.example.task.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.example.task.Model.Subway;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ContentManager {

    private static ContentManager _instance;
    public ArrayList<Subway>Subways = new ArrayList<Subway>();

    public ContentManager() {
    }

    public static ContentManager getInstance() {

        if (_instance == null) {
            _instance = new ContentManager();
        }

        return _instance;

    }



    public ArrayList<Subway> getSubways() {
        return Subways;
    }

    public void setSubways(ArrayList<Subway> subways) {
        Subways = subways;
    }
}
