package com.bcp.falcon.reto.backendservices.notification.business;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.UUID;

import com.bcp.falcon.reto.backendservices.notification.model.UserModel;
import com.bcp.falcon.reto.backendservices.notification.repository.model.NotificationModel;
import com.bcp.falcon.reto.backendservices.notification.repository.NotificationRepository;
import com.bcp.falcon.reto.backendservices.notification.util.constants.EmailTypes;
import com.bcp.falcon.reto.backendservices.notification.util.constants.NotificationTypes;
import com.bcp.falcon.reto.backendservices.notification.util.user.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${notification.mail.from}")
    private String from;

    @Value("${notification.mail.welcome.subject}")
    private String subject;

    @Value("${notification.mail.welcome.template}")
    private String welcomeTemplate;

    @Value("${notification.mail.payment.template}")
    private String paymentTemplate;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserUtil userUtil;

    @Override
    public String sendEmail(String username, String emailType) {

        UserModel userModel = userUtil.getUser(username);

        MimeMessage message = emailSender.createMimeMessage();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setCode(UUID.randomUUID().toString());
        notificationModel.setNotificationType(NotificationTypes.EMAIL.getCode());

        try {
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(userModel.getEmail()));
            message.addFrom(new InternetAddress[]{new InternetAddress(from)});
            message.setSubject(subject, "UTF-8");

            if (emailType.equals(EmailTypes.WELCOME.getCode())) {
                message.setContent(buildWelcomeMailBody(userModel.getName() + " " + userModel.getLastName()),
                        "text/html; charset=utf-8");

                notificationModel.setTemplateName(welcomeTemplate);
            } else if (emailType.equals(EmailTypes.PAYMENT.getCode())) {
                message.setContent(buildPaymentMailBody(), "text/html; charset=utf-8");

                notificationModel.setTemplateName(paymentTemplate);
            }

//            emailSender.send(message);
            notificationModel.setSentDate(new Timestamp(System.currentTimeMillis()));

            notificationRepository.save(notificationModel);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return notificationModel.getCode();
    }

    @Override
    public String retrieveEmail(String username, String code) {
        String response = null;

        UserModel userModel = userUtil.getUser(username);

        NotificationModel notificationModel = notificationRepository.findByCode(code);

        String templateName = notificationModel.getTemplateName();

        if (templateName.equals(EmailTypes.WELCOME.getDescription())) {
            response = buildWelcomeMailBody(userModel.getName() + " " + userModel.getLastName());
        } else if (templateName.equals(EmailTypes.PAYMENT.getDescription())) {
            response = buildPaymentMailBody();
        }

        return response;
    }

    private String buildWelcomeMailBody(String name) {
        String parsedText = "";
        try {
            Resource resource = resourceLoader.getResource("classpath:" + welcomeTemplate);
            File file = resource.getFile();
            String content = new String(Files.readAllBytes(file.toPath()));
            parsedText = String.format(content, name);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parsedText;
    }

    private String buildPaymentMailBody() {
        String parsedText = "";
        try {
            Resource resource = resourceLoader.getResource("classpath:" + paymentTemplate);
            File file = resource.getFile();
            String content = new String(Files.readAllBytes(file.toPath()));
            parsedText = String.format(content, "Usuario 1", "Cod 1", "15/02/2020", "15/04/2020");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parsedText;
    }
}
