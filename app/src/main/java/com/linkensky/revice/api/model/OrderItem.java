package com.linkensky.revice.api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class OrderItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("problem_type")
    @Expose
    private String problemType;
    @SerializedName("problem_desc")
    @Expose
    private String problemDesc;
    @SerializedName("location_desc")
    @Expose
    private String locationDesc;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_data")
    @Expose
    private UserData userData;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The problemType
     */
    public String getProblemType() {
        return problemType;
    }

    /**
     *
     * @param problemType
     * The problem_type
     */
    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    /**
     *
     * @return
     * The problemDesc
     */
    public String getProblemDesc() {
        return problemDesc;
    }

    /**
     *
     * @param problemDesc
     * The problem_desc
     */
    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    /**
     *
     * @return
     * The locationDesc
     */
    public String getLocationDesc() {
        return locationDesc;
    }

    /**
     *
     * @param locationDesc
     * The location_desc
     */
    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The lng
     */
    public String getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     * The lng
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     *
     * @param userData
     * The user_data
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}