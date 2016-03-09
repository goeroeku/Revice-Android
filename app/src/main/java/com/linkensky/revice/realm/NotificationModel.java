package com.linkensky.revice.realm;

import io.realm.RealmObject;

/**
 * Created by setyo on 09/03/16.
 */
public class NotificationModel extends RealmObject {
    private String title;
    private String desc;
    private String datetime;
    private Integer type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
