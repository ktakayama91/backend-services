package com.bcp.falcon.reto.backendservices.notification.business;


import javax.mail.internet.MimeMessage;

import com.bcp.falcon.reto.backendservices.customer.business.CustomerService;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.EmailWelcomeRequest;
import com.bcp.falcon.reto.backendservices.notification.repository.EmailNotificationRepository;
import com.bcp.falcon.reto.backendservices.notification.util.user.UserUtil;
import com.bcp.falcon.reto.backendservices.payment.business.PaymentService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    public JavaMailSender emailSender;

    @Mock
    private EmailNotificationRepository emailNotificationRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserUtil userUtil;

    @Mock
    private SpringTemplateEngine templateEngine;



}