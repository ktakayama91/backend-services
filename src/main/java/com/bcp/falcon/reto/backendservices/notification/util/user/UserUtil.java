package com.bcp.falcon.reto.backendservices.notification.util.user;

import com.bcp.falcon.reto.backendservices.notification.model.UserModel;
import com.bcp.falcon.reto.backendservices.security.repository.UserDetailsRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Component
public class UserUtil {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public UserModel getUser(String username) {
        UserDetailsModel userDetailsModel = userDetailsRepository.findByUsername(username);

        UserModel userModel = new UserModel(userDetailsModel.getName(),
                userDetailsModel.getLastName(),
                userDetailsModel.getEmail(),
                userDetailsModel.getMobile());

        return userModel;
    }
}
