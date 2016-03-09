package com.linkensky.revice.realm;

import io.realm.RealmObject;

/**
 * Created by setyo on 09/03/16.
 */
public class JobModel extends RealmObject {
    private String id;
    private String idPemesan;
    private String namaPemesan;
    private Integer status;
    private String waktu;
    private String problemType;

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPemesan() {
        return idPemesan;
    }

    public void setIdPemesan(String idPemesan) {
        this.idPemesan = idPemesan;
    }

    public String getNamaPemesan() {
        return namaPemesan;
    }

    public void setNamaPemesan(String namaPemesan) {
        this.namaPemesan = namaPemesan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
