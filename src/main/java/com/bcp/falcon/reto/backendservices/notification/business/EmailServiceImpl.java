package com.bcp.falcon.reto.backendservices.notification.business;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bcp.falcon.reto.backendservices.customer.business.CustomerService;
import com.bcp.falcon.reto.backendservices.customer.repository.model.CustomerModel;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.EmailWelcomeRequest;
import com.bcp.falcon.reto.backendservices.notification.model.UserModel;
import com.bcp.falcon.reto.backendservices.notification.repository.EmailNotificationRepository;
import com.bcp.falcon.reto.backendservices.notification.repository.model.EmailNotificationModel;
import com.bcp.falcon.reto.backendservices.notification.util.constants.EmailTypes;
import com.bcp.falcon.reto.backendservices.payment.business.PaymentService;
import com.bcp.falcon.reto.backendservices.payment.repository.model.PaymentModel;
import com.bcp.falcon.reto.backendservices.notification.util.constants.NotificationTypes;
import com.bcp.falcon.reto.backendservices.notification.util.user.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${notification.mail.from}")
    private String from;

    @Value("${notification.mail.welcome.subject}")
    private String welcomeSubject;

    @Value("${notification.mail.payment.subject}")
    private String paymentSubject;

    @Value("${notification.mail.welcome.template}")
    private String welcomeTemplate;

    @Value("${notification.mail.payment.template}")
    private String paymentTemplate;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private EmailNotificationRepository emailNotificationRepository;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public String sendWelcomeEmail(String username, EmailWelcomeRequest emailWelcomeRequest) {

        MimeMessage message = emailSender.createMimeMessage();


        EmailNotificationModel emailNotificationModel = new EmailNotificationModel();
        emailNotificationModel.setCode(UUID.randomUUID().toString());

        CustomerModel customerModel = new CustomerModel();
        customerModel.setName(emailWelcomeRequest.getName());
        customerModel.setLastName(emailWelcomeRequest.getLastName());
        customerModel.setEmail(emailWelcomeRequest.getReceiptEmail());

        Long customerId = customerService.saveCustomer(customerModel);

        try {
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailWelcomeRequest.getReceiptEmail()));
            message.addFrom(new InternetAddress[]{new InternetAddress(from)});
            message.setSubject(welcomeSubject, "UTF-8");


            message.setContent(buildWelcomeMailBody(emailWelcomeRequest.getName() + " " + emailWelcomeRequest.getLastName()),
                    "text/html; charset=utf-8");

            emailNotificationModel.setNotificationType(NotificationTypes.EMAIL.getCode());
            emailNotificationModel.setTemplateName(welcomeTemplate);
            emailNotificationModel.setRecipientEmail(emailWelcomeRequest.getReceiptEmail());
            emailNotificationModel.setRecipientUserId(customerId);

            emailSender.send(message);

            emailNotificationModel.setSentDate(new Timestamp(System.currentTimeMillis()));

            emailNotificationRepository.save(emailNotificationModel);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return emailNotificationModel.getCode();
    }

    @Override
    public String sendPaymentEmail(String username) {

        UserModel userModel = userUtil.getUser(username);

        List<PaymentModel> paymentModels = paymentService.getPayments(userModel.getEmail());

        MimeMessage message = emailSender.createMimeMessage();

        EmailNotificationModel emailNotificationModel = new EmailNotificationModel();
        emailNotificationModel.setCode(UUID.randomUUID().toString());
        emailNotificationModel.setNotificationType(NotificationTypes.EMAIL.getCode());
        emailNotificationModel.setRecipientEmail(userModel.getEmail());

        try {
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(userModel.getEmail()));
            message.addFrom(new InternetAddress[]{new InternetAddress(from)});
            message.setSubject(paymentSubject, "UTF-8");

            message.setContent(buildPaymentMailBody(userModel.getName(), paymentModels), "text/html; charset=utf-8");

            emailNotificationModel.setTemplateName(paymentTemplate);

            emailSender.send(message);

            emailNotificationModel.setSentDate(new Timestamp(System.currentTimeMillis()));

            emailNotificationRepository.save(emailNotificationModel);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return emailNotificationModel.getCode();
    }

    @Override
    public String retrieveEmail(String username, String code) {
        String response = null;

        Optional<EmailNotificationModel> emailNotificationModel = emailNotificationRepository.findByCode(code);

        if (emailNotificationModel.isPresent()) {
            EmailNotificationModel currentEmailNotificationModel = emailNotificationModel.get();

            String templateName = currentEmailNotificationModel.getTemplateName();

            if (templateName.equals(EmailTypes.WELCOME.getDescription())) {
                CustomerModel customerModel = customerService.getCustomer(currentEmailNotificationModel.getRecipientUserId());
                response = buildWelcomeMailBody(customerModel.getName() + " " + customerModel.getLastName());

            } else if (templateName.equals(EmailTypes.PAYMENT.getDescription())) {

                UserModel userModel = userUtil.getUser(username);
                List<PaymentModel> paymentModels = paymentService.getPayments(userModel.getEmail());

                response = buildPaymentMailBody(userModel.getEmail(), paymentModels);
            }
        } else {
            response = "El código enviado no existe";
        }

        return response;
    }

    private String buildWelcomeMailBody(String name) {
        String html = "";
        try {
            Context context = new Context();
            context.setVariable("name", name);
            html = templateEngine.process("welcome", context);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return html;
    }

    private String buildPaymentMailBody(String name, List<PaymentModel> paymentModel) {
        String html = "";
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("payment", paymentModel);
            html = templateEngine.process("payment", context);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return html;
    }
}
