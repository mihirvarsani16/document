package com.document.document.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int udid;

    private String d_name;
    private String image;
    private int filesize;
    private String contentType;

    @OneToOne
    @JsonIgnore
    private User user;

    public UserDocument() {
    }

    public int getUdid() {
        return udid;
    }

    public void setUdid(int udid) {
        this.udid = udid;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
