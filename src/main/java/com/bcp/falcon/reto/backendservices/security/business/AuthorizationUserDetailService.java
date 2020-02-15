package com.bcp.falcon.reto.backendservices.security.business;

import com.bcp.falcon.reto.backendservices.security.repository.UserSessionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/13/20.
 */

@Service
public class AuthorizationUserDetailService implements UserDetailsService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userSessionRepository.findByUsername(username);
    }
}
