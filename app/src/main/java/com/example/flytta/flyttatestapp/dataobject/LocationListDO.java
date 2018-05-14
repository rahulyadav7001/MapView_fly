package com.example.flytta.flyttatestapp.dataobject;

import java.util.ArrayList;

/**
 * Created by WIN 1O on 18-03-2018.
 */

public class LocationListDO extends ArrayList<LocationDO> {
    private ArrayList<LocationDO> arrayListLoaction = null;

    public ArrayList<LocationDO> getArrayListLoaction() {
        return arrayListLoaction;
    }

    public void setArrayListLoaction(ArrayList<LocationDO> arrayListLoaction) {
        this.arrayListLoaction = arrayListLoaction;
    }
}
