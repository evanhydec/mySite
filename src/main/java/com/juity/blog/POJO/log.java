package com.juity.blog.POJO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("log")
public class log {
    @Field("action")
    private String action;
    @Field("data")
    private String data;
    @Field("author_id")
    private Integer authorId;
    @Field("ip")
    private String ip;
    @Field("created")
    private Date created;

    @Override
    public String toString() {
        return "log{" +
                ", action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", authorId=" + authorId +
                ", ip=" + ip +
                ", created=" + created +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public log() {
    }

    public log(String action, String data, Integer authorId, String ip, Date created) {
        this.action = action;
        this.data = data;
        this.authorId = authorId;
        this.ip = ip;
        this.created = created;
    }
}
