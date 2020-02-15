package com.bcp.falcon.reto.backendservices.notification.util.constants;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public enum NotificationTypes {

    EMAIL("Email", 1),
    SMS("SMS", 2),
    PUSH_NOTIFICATION("Push", 3);

    private int code;

    private String description;

    NotificationTypes(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
