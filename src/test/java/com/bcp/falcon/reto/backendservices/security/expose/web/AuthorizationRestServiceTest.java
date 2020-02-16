package com.bcp.falcon.reto.backendservices.security.expose.web;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bcp.falcon.reto.backendservices.security.business.AuthorizationService;
import com.bcp.falcon.reto.backendservices.security.expose.web.request.AuthenticationRequest;
import com.bcp.falcon.reto.backendservices.security.model.AccessToken;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizationRestService.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorizationRestServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    private Gson gson = new Gson();

    @Test
    public void authorizationSuccessTest() throws Exception {

        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken("access_token");
        Mockito.when(authorizationService.generateToken(Mockito.anyString(), Mockito.anyString())).thenReturn(accessToken);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUser("username");
        authenticationRequest.setPassword("password");

        mockMvc.perform(post("/api/falcon/services/security-services/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", CoreMatchers.is("access_token")));
    }

}