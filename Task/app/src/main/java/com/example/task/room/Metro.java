package com.example.task.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "metro")
public class Metro {

    @PrimaryKey
    public int metro_id;

    //역이름
    @ColumnInfo(name = "name")
    public String name;

    //상세주소
    @ColumnInfo(name = "real_path")
    public String realPath;

    //주소
    @ColumnInfo(name = "address")
    public String address;

    //호선
    @ColumnInfo(name = "line")
    public String line;

    public Metro(int metro_id, String name, String realPath, String address, String line) {
        this.metro_id = metro_id;
        this.name = name;
        this.realPath = realPath;
        this.address = address;
        this.line = line;
    }

    public int getMetro_id() {
        return metro_id;
    }

    public void setMetro_id(int metro_id) {
        this.metro_id = metro_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
