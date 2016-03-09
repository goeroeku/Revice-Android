package com.linkensky.revice.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceParcelable implements Parcelable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getTipeName() {
        return tipeName;
    }

    public void setTipeName(String tipeName) {
        this.tipeName = tipeName;
    }

    private String id;
    private String nama;
    private String alamat;
    private String deskripsi;
    private String telp;
    private String tipeName;

    public ServiceParcelable(String id, String nama, String alamat, String deskripsi, String telp, String tipeName) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.deskripsi = deskripsi;
        this.telp = telp;
        this.tipeName = tipeName;
    }

    @Override

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.id,
                this.nama,
                this.alamat,
                this.deskripsi,
                this.telp,
                this.tipeName
        });
    }

    public ServiceParcelable(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.id = data[0];
        this.nama = data[1];
        this.alamat = data[2];
        this.deskripsi = data[3];
        this.telp = data[4];
        this.tipeName = data[5];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ServiceParcelable createFromParcel(Parcel in) {
            return new ServiceParcelable(in);
        }

        public ServiceParcelable[] newArray(int size) {
            return new ServiceParcelable[size];
        }
    };
}