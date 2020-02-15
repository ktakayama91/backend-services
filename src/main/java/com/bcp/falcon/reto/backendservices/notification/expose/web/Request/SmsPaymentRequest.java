package com.bcp.falcon.reto.backendservices.notification.expose.web.Request;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public class SmsPaymentRequest {

    private String operationCode;

    private String payerName;

    private String paymentDate;

    private String receiptPhoneNumber;

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReceiptPhoneNumber() {
        return receiptPhoneNumber;
    }

    public void setReceiptPhoneNumber(String receiptPhoneNumber) {
        this.receiptPhoneNumber = receiptPhoneNumber;
    }
}
