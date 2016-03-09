package com.linkensky.revice.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderRequest {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("dist")
    @Expose
    private Double dist;
    @SerializedName("max")
    @Expose
    private Integer max;

    public OrderRequest(String type, String desc, String location, String lat, String lng, Double dist, Integer max) {
        this.type = type;
        this.desc = desc;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.dist = dist;
        this.max = max;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
