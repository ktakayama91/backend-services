package com.bcp.falcon.reto.backendservices.notification.business;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import com.bcp.falcon.reto.backendservices.notification.client.TwilioClient;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.SmsPaymentRequest;
import com.bcp.falcon.reto.backendservices.notification.model.UserModel;
import com.bcp.falcon.reto.backendservices.notification.repository.NotificationRepository;
import com.bcp.falcon.reto.backendservices.notification.util.constants.SmsTypes;
import com.bcp.falcon.reto.backendservices.payment.repository.PaymentRepository;
import com.bcp.falcon.reto.backendservices.notification.repository.model.NotificationModel;
import com.bcp.falcon.reto.backendservices.payment.repository.model.PaymentModel;
import com.bcp.falcon.reto.backendservices.notification.util.constants.NotificationTypes;
import com.bcp.falcon.reto.backendservices.notification.util.user.UserUtil;
import com.bcp.falcon.reto.backendservices.security.business.OtpServiceImpl;

import com.bcp.falcon.reto.backendservices.security.repository.UserOtpRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserOtpModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private TwilioClient twilioClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OtpServiceImpl otpServiceImpl;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserOtpRepository userOtpRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${notification.sms.otp.template}")
    private String otpMessage;

    @Value("${notification.sms.payment.template}")
    private String paymentMessage;

    @Override
    public void sendOtpSms(String username) {

        UserModel userModel = userUtil.getUser(username);

        String otp = otpServiceImpl.generateOtp(username);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setUser(username);
        notificationModel.setCode(UUID.randomUUID().toString());
        notificationModel.setTemplateName(otpMessage);
        notificationModel.setNotificationType(NotificationTypes.SMS.getCode());
        notificationModel.setSentDate(new Timestamp(System.currentTimeMillis()));
        notificationModel.setRecipient(userModel.getMobile());

        NotificationModel notificationModelSaved = notificationRepository.save(notificationModel);

        Optional<UserOtpModel> userOtpModel = userOtpRepository.findByUsernameAndActive(username, true);
        userOtpModel.get().setNotification_id(notificationModelSaved.getId());

        userOtpRepository.save(userOtpModel.get());

        String message = this.buildOtpMessage(userModel.getName(), otp);

        twilioClient.sendSms(message, userModel.getMobile());
    }

    @Override
    public void sendPaymentSms(String username, SmsPaymentRequest smsPaymentRequest) {

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setUser("");
        notificationModel.setCode(UUID.randomUUID().toString());
        notificationModel.setTemplateName(paymentMessage);
        notificationModel.setNotificationType(NotificationTypes.SMS.getCode());
        notificationModel.setSentDate(new Timestamp(System.currentTimeMillis()));
        notificationModel.setRecipient(smsPaymentRequest.getReceiptPhoneNumber());
        notificationModel.setUser(username);

        NotificationModel notificationModelSaved = notificationRepository.save(notificationModel);

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setOperationCode(smsPaymentRequest.getOperationCode());
        paymentModel.setPayerName(smsPaymentRequest.getPayerName());
        paymentModel.setPaymentDate(smsPaymentRequest.getPaymentDate());
        paymentModel.setNotificationId(notificationModelSaved.getId());

        paymentRepository.save(paymentModel);

        String message = this.buildPaymentMessage(smsPaymentRequest.getOperationCode(),
                smsPaymentRequest.getPayerName(),
                smsPaymentRequest.getPaymentDate());
        twilioClient.sendSms(message, smsPaymentRequest.getReceiptPhoneNumber());

    }

    @Override
    public String retrieveSms(String code) {
        String response = null;

        NotificationModel notificationModel = notificationRepository.findByCode(code);

        UserModel userModel = userUtil.getUser(notificationModel.getUser());

        String templateName = notificationModel.getTemplateName();

        if (templateName.equals(SmsTypes.OTP.getDescription())) {

            UserOtpModel userOtpModel = userOtpRepository.findByNotificationId(notificationModel.getId());
            response = buildOtpMessage(userModel.getName() + " " + userModel.getLastName(), userOtpModel.getOtp());

        } else if (templateName.equals(SmsTypes.PAYMENT.getDescription())) {

            PaymentModel paymentModel = paymentRepository.findByNotificationId(notificationModel.getId());
            response = buildPaymentMessage(paymentModel.getOperationCode(), paymentModel.getPayerName(), paymentModel.getPaymentDate());
        }

        return response;
    }

    private String buildOtpMessage(String name, String otp) {
        String text = "";
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("otp", otp);
            text = templateEngine.process("otp", context);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }

    protected String buildPaymentMessage(String operationCode, String payerName, String paymentDate) {
        String text = "";
        try {
            Context context = new Context();
            context.setVariable("operationCode", operationCode);
            context.setVariable("payerName", payerName);
            context.setVariable("paymentDate", paymentDate);
            text = templateEngine.process("sms_payment", context);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }
}
