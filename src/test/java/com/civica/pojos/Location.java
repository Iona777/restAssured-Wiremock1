package com.civica.pojos;

//Uses POJO (Plain Old Java Class) to define class
//You can then use the Getter and Setter methods below for accessing the data
public class Location  //Location is not a single string or integer, so define as a its own POJO class
{
    //These are the data element that make up the Location object
    double lat;
    double lng;

    //Getters
    public double getLat()
    {
        return lat;
    }

    public double getLng()
    {
        return lng;
    }

    //Setters
    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }

}

