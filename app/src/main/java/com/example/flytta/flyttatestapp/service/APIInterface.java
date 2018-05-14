package com.example.flytta.flyttatestapp.service;

import com.example.flytta.flyttatestapp.dataobject.DetailLocationDO;
import com.example.flytta.flyttatestapp.dataobject.LocationDO;
import com.example.flytta.flyttatestapp.dataobject.LocationListDO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Flytta on 17-03-2018.
 */

public interface APIInterface {
    @GET("/flytta_api/v0.1/portal/showlatlon")
    Call<LocationListDO> getLocationsFromServer();

    @GET("/flytta_api/v0.1/portal/property/findserviceid/{service_id}")
    Call<DetailLocationDO> getLoactionDetail(@Path("service_id") String service_id);
}
