package com.example.task.Utils;

import com.example.task.Model.SubwayModel.Subway;

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
