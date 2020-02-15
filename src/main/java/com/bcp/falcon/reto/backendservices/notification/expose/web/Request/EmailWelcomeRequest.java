package com.bcp.falcon.reto.backendservices.notification.expose.web.Request;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public class EmailWelcomeRequest {

    private String name;

    private String lastName;

    private String receiptEmail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }
}
