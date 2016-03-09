package com.linkensky.revice.api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class User {

    @SerializedName("user")
    @Expose
    private UserItem user;

    /**
     *
     * @return
     * The user
     */
    public UserItem getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(UserItem user) {
        this.user = user;
    }

}