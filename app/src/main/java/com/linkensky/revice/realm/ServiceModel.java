package com.linkensky.revice.realm;

import io.realm.RealmObject;

/**
 * Created by setyo on 09/03/16.
 */
public class ServiceModel extends RealmObject {
    private String id;
    private String nama;
    private String tipe;

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

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}
