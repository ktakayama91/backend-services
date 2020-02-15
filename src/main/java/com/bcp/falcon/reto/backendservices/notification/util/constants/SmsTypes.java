package com.bcp.falcon.reto.backendservices.notification.util.constants;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public enum SmsTypes {

    OTP("otp.txt", "S01"),
    PAYMENT("payment.txt", "S02");

    private String code;

    private String description;

    SmsTypes(String description, String code) {
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
