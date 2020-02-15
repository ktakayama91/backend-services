package com.bcp.falcon.reto.backendservices.security.expose.web.request;

/**
 * @author Kei Takayama
 * Created on 2/13/20.
 */
public class AuthenticationRequest {

    private String user;
    private String password;

    public AuthenticationRequest() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
