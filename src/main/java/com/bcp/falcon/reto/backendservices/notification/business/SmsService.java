package com.bcp.falcon.reto.backendservices.notification.business;

import java.io.File;
import java.nio.file.Files;

import com.bcp.falcon.reto.backendservices.notification.client.TwilioClient;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.SmsPaymentRequest;
import com.bcp.falcon.reto.backendservices.notification.model.UserModel;
import com.bcp.falcon.reto.backendservices.notification.util.user.UserUtil;
import com.bcp.falcon.reto.backendservices.security.business.OtpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Service
public class SmsService {

    @Autowired
    private TwilioClient twilioClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserUtil userUtil;

    @Value("${notification.sms.otp.template}")
    private String otpMessage;

    @Value("${notification.sms.payment.template}")
    private String paymentMessage;

    public void sendSms(String username) {

        UserModel userModel = userUtil.getUser(username);

        String otp = otpService.generateOtp(username);

        String message = this.buildOtpMessage(userModel.getName(), otp);

        twilioClient.sendSms(message, userModel.getMobile());
    }

    public void sendSms(SmsPaymentRequest smsPaymentRequest) {
        String message = this.buildPaymentMessage(smsPaymentRequest.getOperationCode(),
                smsPaymentRequest.getPayerName(),
                smsPaymentRequest.getPaymentDate());
        twilioClient.sendSms(message, smsPaymentRequest.getReceiptPhoneNumber());
    }

    private String buildOtpMessage(String name, String otp) {
        String parsedText = "";

        try {
            Resource resource = resourceLoader.getResource("classpath:" + otpMessage);
            File file = resource.getFile();
            String content = new String(Files.readAllBytes(file.toPath()));
            parsedText = String.format(content, name, otp);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parsedText;
    }

    private String buildPaymentMessage(String operationCode, String payerName, String paymentDate) {
        String parsedText = "";

        try {
            Resource resource = resourceLoader.getResource("classpath:" + paymentMessage);
            File file = resource.getFile();
            String content = new String(Files.readAllBytes(file.toPath()));
            parsedText = String.format(content, operationCode, payerName, paymentDate);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parsedText;
    }
}
