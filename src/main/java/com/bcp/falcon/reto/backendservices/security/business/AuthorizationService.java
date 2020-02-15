package com.bcp.falcon.reto.backendservices.security.business;

import com.bcp.falcon.reto.backendservices.security.model.AccessToken;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserDetailsModel;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserSessionModel;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public interface AuthorizationService {

    AccessToken generateToken(String username, String password);

    UserDetailsModel authenticate(String username, String password);

    UserSessionModel createUserSession(String username, String password);

    void updateAccessToken(String username, String accessToken);
}
