package com.linkensky.revice.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by setyo on 02/03/16.
 */
public class StatusRequest {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("service_id")
    @Expose
    private String serviceId;

    public StatusRequest(Integer status, String serviceId) {
        this.status = status;
        this.serviceId = serviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
