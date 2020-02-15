package com.bcp.falcon.reto.backendservices.notification.model;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public class UserModel {

    private String name;

    private String lastName;

    private String email;

    private String mobile;

    public UserModel(String name, String lastName, String email, String mobile) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
