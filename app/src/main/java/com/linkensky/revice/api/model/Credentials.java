package com.linkensky.revice.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by setyo on 04/02/16.
 */
public class Credentials {

    @SerializedName("email")
    String mEmail;

    @SerializedName("password")
    String mPassword;

    public Credentials(String email, String password ) {
        this.mEmail = email;
        this.mPassword = password;
    }
}
