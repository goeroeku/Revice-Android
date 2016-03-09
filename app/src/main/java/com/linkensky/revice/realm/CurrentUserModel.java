package com.linkensky.revice.realm;


import io.realm.RealmList;
import io.realm.RealmObject;

public class CurrentUserModel extends RealmObject {
    private String id;
    private String nama;
    private String email;
    private String alamat;
    private String telp;
    private String verified;
    private String roleName;
    private RealmList<ServiceModel> service;

    public RealmList<ServiceModel> getService() {
        return service;
    }

    public void setService(RealmList<ServiceModel> service) {
        this.service = service;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
