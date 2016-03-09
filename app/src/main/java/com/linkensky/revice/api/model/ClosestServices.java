package com.linkensky.revice.api.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ClosestServices {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("data")
    @Expose
    private List<ServiceItem> data = new ArrayList<ServiceItem>();

    /**
     *
     * @return
     * The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     *
     * @return
     * The data
     */
    public List<ServiceItem> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<ServiceItem> data) {
        this.data = data;
    }

}