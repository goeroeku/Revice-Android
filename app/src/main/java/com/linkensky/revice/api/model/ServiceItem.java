package com.linkensky.revice.api.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("telp")
    @Expose
    private String telp;
    @SerializedName("tipe_id")
    @Expose
    private String tipeId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("tipe_name")
    @Expose
    private String tipeName;
    @SerializedName("admin_id")
    @Expose
    private List<String> adminId = new ArrayList<String>();

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
     * The nama
     */
    public String getNama() {
        return nama;
    }

    /**
     *
     * @param nama
     * The nama
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     *
     * @return
     * The alamat
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     *
     * @param alamat
     * The alamat
     */
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     *
     * @return
     * The deskripsi
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     *
     * @param deskripsi
     * The deskripsi
     */
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    /**
     *
     * @return
     * The telp
     */
    public String getTelp() {
        return telp;
    }

    /**
     *
     * @param telp
     * The telp
     */
    public void setTelp(String telp) {
        this.telp = telp;
    }

    /**
     *
     * @return
     * The tipeId
     */
    public String getTipeId() {
        return tipeId;
    }

    /**
     *
     * @param tipeId
     * The tipe_id
     */
    public void setTipeId(String tipeId) {
        this.tipeId = tipeId;
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
     * The tipeName
     */
    public String getTipeName() {
        return tipeName;
    }

    /**
     *
     * @param tipeName
     * The tipe_name
     */
    public void setTipeName(String tipeName) {
        this.tipeName = tipeName;
    }

    /**
     *
     * @return
     * The adminId
     */
    public List<String> getAdminId() {
        return adminId;
    }

    /**
     *
     * @param adminId
     * The admin_id
     */
    public void setAdminId(List<String> adminId) {
        this.adminId = adminId;
    }

}