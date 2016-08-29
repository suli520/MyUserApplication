package com.namikj.proj.myuserapplication;

import java.io.Serializable;

/**
 * Created by michael on 2016/8/24.
 */
public class User implements Serializable
{
    private Integer id;
    private String name;
    private String password;
    private String tel;
    private String photo;
    public User(){}
    public User(String name,String pw)
    {
        this.name=name;
        this.password=pw;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
