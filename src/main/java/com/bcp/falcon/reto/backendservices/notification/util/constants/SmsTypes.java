package com.bcp.falcon.reto.backendservices.notification.util.constants;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public enum SmsTypes {

    OTP("otp.html", "S01"),
    PAYMENT("sms_payment.html", "S02");

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
