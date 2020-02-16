package com.bcp.falcon.reto.backendservices.notification.expose.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bcp.falcon.reto.backendservices.notification.business.EmailServiceImpl;
import com.bcp.falcon.reto.backendservices.notification.business.SmsServiceImpl;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.EmailWelcomeRequest;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.SmsPaymentRequest;
import com.bcp.falcon.reto.backendservices.payment.repository.model.PaymentModel;
import com.bcp.falcon.reto.backendservices.security.expose.web.AuthorizationRestService;
import com.google.gson.Gson;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(NotificationRestService.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationRestServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EmailServiceImpl emailService;

    @MockBean
    SmsServiceImpl smsService;

    Gson gson = new Gson();

    @Test
    @WithUserDetails
    public void sendWelcomeEmailSuccessTest() throws Exception {

        EmailWelcomeRequest emailWelcomeRequest = new EmailWelcomeRequest();
        emailWelcomeRequest.setName("name");
        emailWelcomeRequest.setLastName("lastName");
        emailWelcomeRequest.setReceiptEmail("email@email.com");

        Mockito.when(emailService.sendWelcomeEmail(Mockito.anyString(), Mockito.any())).thenReturn("UUI-Random-Code");

        mockMvc.perform(post("/api/falcon/services/notification-services/v1/email/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(emailWelcomeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is("UUI-Random-Code")));
    }

    @Test
    @WithUserDetails
    public void sendWelcomePaymentSuccessTest() throws Exception {

        Mockito.when(emailService.sendPaymentEmail(Mockito.anyString())).thenReturn("UUI-Random-Code");

        mockMvc.perform(get("/api/falcon/services/notification-services/v1/email/payment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is("UUI-Random-Code")));
    }

    @Test
    @WithUserDetails
    public void retrieveEmailSuccessTest() throws Exception {

        Mockito.when(emailService.retrieveEmail(Mockito.anyString(), Mockito.anyString())).thenReturn("Email Retrieved");

        mockMvc.perform(get("/api/falcon/services/notification-services/v1/email/retrieve/{code}","code")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is("Email Retrieved")));
    }

    @Test
    @WithUserDetails
    public void sendOtpSmsSuccessTest() throws Exception {

        mockMvc.perform(get("/api/falcon/services/notification-services/v1/sms/otp")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    public void sendPaymentSmsSuccessTest() throws Exception {

        SmsPaymentRequest smsPaymentRequest = new SmsPaymentRequest();
        smsPaymentRequest.setOperationCode("OpeCode");
        smsPaymentRequest.setPayerName("Pname");
        smsPaymentRequest.setPaymentDate("Pdate");
        smsPaymentRequest.setReceiptPhoneNumber("+51123456789");

        mockMvc.perform(post("/api/falcon/services/notification-services/v1/sms/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(smsPaymentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    public void retrieveSmsSuccessTest() throws Exception {

        Mockito.when(smsService.retrieveSms(Mockito.anyString())).thenReturn("Sms Retrieved");

        mockMvc.perform(get("/api/falcon/services/notification-services/v1/sms/retrieve/{code}","code")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is("Sms Retrieved")));
    }


}