package com.document.document.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private UserDocument userDocuments;

    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDocument getUserDocuments() {
        return userDocuments;
    }

    public void setUserDocuments(UserDocument userDocuments) {
        this.userDocuments = userDocuments;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", uid=" + uid + ", userDocuments=" + userDocuments + "]";
    }

}
