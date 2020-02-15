package com.bcp.falcon.reto.backendservices.security.business;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bcp.falcon.reto.backendservices.security.model.AccessToken;
import com.bcp.falcon.reto.backendservices.security.repository.UserDetailsRepository;
import com.bcp.falcon.reto.backendservices.security.repository.UserSessionRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserDetailsModel;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserSessionModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kei Takayama
 * Created on 2/13/20.
 */

@SuppressWarnings("deprecation")
@Service
public class AuthorizationService {

    private String urlAccessToken = "http://localhost:8080/oauth/token?grant_type={grant_type}&username={username}&password={password}";

    private String clientUser = "client-user-1";

    private String clientSecret = "client-secret-1";

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

//    @Autowired
//    private TokenStore tokenStore;

    public AccessToken generateToken(String username, String password) {
        HttpEntity<String> entity = new HttpEntity<>("parameters", buildAuthHeaders());

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("grant_type", "password");
        queryParams.put("username", username);
        queryParams.put("password", password);

        ResponseEntity<AccessToken> result = new RestTemplate().exchange(urlAccessToken, HttpMethod.POST, entity,
                        AccessToken.class, queryParams);

        return result.getBody();
    }

    private HttpHeaders buildAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String oauthCredentials = clientUser + ":" + clientSecret;
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(oauthCredentials.getBytes()));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        return headers;
    }

//    public AccessToken generateRefreshToken(String refreshToken) {
//        HttpEntity<String> entity = new HttpEntity<>("parameters", buildAuthHeaders());
//
//        Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("grant_type", "refresh_token");
//        queryParams.put("refresh_token", refreshToken);
//
//        ResponseEntity<AccessToken> result = new RestTemplate().exchange(urlRefreshToken, HttpMethod.POST, entity,
//                AccessToken.class, queryParams);
//
//        return result.getBody();
//    }
//
//    public boolean revokeToken(final String token, final String tokenHint) {
//        if (tokenHint != null && "refresh_token".equals(tokenHint)) {
//            return revokeRefreshToken(token) || revokeAccessToken(token);
//        }
//
//        return revokeAccessToken(token) || revokeRefreshToken(token);
//    }
//
//    protected boolean revokeRefreshToken(final String token) {
//        final OAuth2RefreshToken oAuth2RefreshToken = this.tokenStore.readRefreshToken(token);
//        if (oAuth2RefreshToken != null) {
//            this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
//            this.tokenStore.removeRefreshToken(oAuth2RefreshToken);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    protected boolean revokeAccessToken(final String token) {
//        final OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(token);
//        if (oAuth2AccessToken != null) {
//            if (oAuth2AccessToken.getRefreshToken() != null) {
//                this.tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
//            }
//
//            this.tokenStore.removeAccessToken(oAuth2AccessToken);
//
//            return true;
//        }
//
//        return false;
//    }

    public UserDetailsModel authenticate(String username, String password) {
        UserDetailsModel userDetailsModel = userDetailsRepository.findByUsernameAndPassword(username, password);
        if (userDetailsModel == null) {
            throw new AccessDeniedException("Username and/or password are invalid");
        }
        return userDetailsModel;
    }

    public UserSessionModel createUserSession(String username, String password) {
        UserSessionModel userSessionModel = userSessionRepository.findByUsername(username);

        if(userSessionModel == null) {
            userSessionModel = new UserSessionModel();
            userSessionModel.setUsername(username);
        }

        userSessionModel.setPassword("{noop}"+password);

        return userSessionRepository.save(userSessionModel);
    }

    public void updateAccessToken(String username, String accessToken) {
        UserSessionModel userSessionModel = userSessionRepository.findByUsername(username);
        if(userSessionModel==null) {
            return;
        }
        userSessionModel.setAccessToken(accessToken);
        userSessionRepository.save(userSessionModel);
    }

}
