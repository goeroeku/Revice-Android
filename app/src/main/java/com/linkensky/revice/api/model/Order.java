package com.linkensky.revice.api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Order {

    @SerializedName("order")
    @Expose
    private OrderItem order;

    /**
     *
     * @return
     * The order
     */
    public OrderItem getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(OrderItem order) {
        this.order = order;
    }

}