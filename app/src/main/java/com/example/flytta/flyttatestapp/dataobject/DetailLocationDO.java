package com.example.flytta.flyttatestapp.dataobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by WIN 1O on 18-03-2018.
 */

public class DetailLocationDO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("item")
    @Expose
    private Item item;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
