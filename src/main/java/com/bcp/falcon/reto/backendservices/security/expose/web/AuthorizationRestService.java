package com.bcp.falcon.reto.backendservices.security.expose.web;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import com.bcp.falcon.reto.backendservices.security.business.AuthorizationService;
import com.bcp.falcon.reto.backendservices.security.expose.web.request.AuthenticationRequest;
import com.bcp.falcon.reto.backendservices.security.model.AccessToken;

import com.bcp.falcon.reto.backendservices.security.repository.model.UserSessionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kei Takayama
 * Created on 2/13/20.
 */

@RestController
@RequestMapping("/api/falcon/services/security-services/v1")
public class AuthorizationRestService {

    @Autowired
    private AuthorizationService authorizationService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public AccessToken authorization(@RequestBody AuthenticationRequest authenticationRequest) {
        authorizationService.authenticate(authenticationRequest.getUser(), authenticationRequest.getPassword());
        authorizationService.createUserSession(authenticationRequest.getUser(), authenticationRequest.getPassword());
        AccessToken accessToken = authorizationService.generateToken(authenticationRequest.getUser(), authenticationRequest.getPassword());
        authorizationService.updateAccessToken(authenticationRequest.getUser(), accessToken.getAccessToken());
        return accessToken;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserSessionModel userSessionModel = (UserSessionModel) auth.getPrincipal();

        return "It's OK";
    }
}
