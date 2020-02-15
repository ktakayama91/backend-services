package com.bcp.falcon.reto.backendservices.notification.business;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.bcp.falcon.reto.backendservices.notification.client.TwilioClient;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.SmsPaymentRequest;
import com.bcp.falcon.reto.backendservices.notification.model.UserModel;
import com.bcp.falcon.reto.backendservices.notification.repository.NotificationRepository;
import com.bcp.falcon.reto.backendservices.notification.repository.model.NotificationModel;
import com.bcp.falcon.reto.backendservices.notification.util.user.UserUtil;
import com.bcp.falcon.reto.backendservices.payment.repository.PaymentRepository;
import com.bcp.falcon.reto.backendservices.security.business.OtpServiceImpl;
import com.bcp.falcon.reto.backendservices.security.repository.UserOtpRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserOtpModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@ExtendWith(MockitoExtension.class)
class SmsServiceImplTest {

    @InjectMocks
    private SmsServiceImpl smsService;

    @Mock
    private TwilioClient twilioClient;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private OtpServiceImpl otpServiceImpl;

    @Mock
    private UserUtil userUtil;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserOtpRepository userOtpRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Test
    public void sendOtpSmsSuccessTest() {

        UserModel userModel = new UserModel("name", "lastName","email", "mobile");
        Mockito.when(userUtil.getUser(Mockito.anyString())).thenReturn(userModel);

        String otp = "1234";
        Mockito.when(otpServiceImpl.generateOtp(Mockito.anyString())).thenReturn(otp);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setId(1L);
        Mockito.when(notificationRepository.save(Mockito.any())).thenReturn(notificationModel);

        Optional<UserOtpModel> userOtpModel = Optional.of(new UserOtpModel());

        Mockito.when(userOtpRepository.findByUsernameAndActive(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(userOtpModel);

        smsService.sendOtpSms("username");

    }

    @Test
    public void sendPaymentSmsSuccessTest() {

        SmsPaymentRequest smsPaymentRequest = new SmsPaymentRequest();
        smsPaymentRequest.setOperationCode("opcode");
        smsPaymentRequest.setPayerName("pname");
        smsPaymentRequest.setPaymentDate("pdate");
        smsPaymentRequest.setReceiptPhoneNumber("+51123456789");

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setId(1L);
        Mockito.when(notificationRepository.save(Mockito.any())).thenReturn(notificationModel);

        smsService.sendPaymentSms("username", smsPaymentRequest);
    }

}