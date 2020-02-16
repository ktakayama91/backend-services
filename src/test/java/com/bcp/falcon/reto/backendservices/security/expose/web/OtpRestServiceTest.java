package com.bcp.falcon.reto.backendservices.security.expose.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import com.bcp.falcon.reto.backendservices.security.business.OtpServiceImpl;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserSessionModel;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(OtpRestService.class)
@AutoConfigureMockMvc(addFilters = false)
class OtpRestServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OtpServiceImpl otpService;

    @Test
    @WithUserDetails
    public void test() throws Exception {
        Mockito.when(otpService.validateOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        mockMvc.perform(get("/api/falcon/services/security-services/otp/validate/{otp}", "1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(true)));
    }

}