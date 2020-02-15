package com.bcp.falcon.reto.backendservices.notification.util.constants;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public enum EmailTypes {

    WELCOME("welcome", "E01"),
    PAYMENT("payment", "E02");

    private String code;

    private String description;

    EmailTypes(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
