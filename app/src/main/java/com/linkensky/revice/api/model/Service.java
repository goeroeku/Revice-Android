package com.linkensky.revice.api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Service {

    @SerializedName("service")
    @Expose
    private ServiceItem service;

    /**
     *
     * @return
     * The service
     */
    public ServiceItem getService() {
        return service;
    }

    /**
     *
     * @param service
     * The service
     */
    public void setService(ServiceItem service) {
        this.service = service;
    }

}