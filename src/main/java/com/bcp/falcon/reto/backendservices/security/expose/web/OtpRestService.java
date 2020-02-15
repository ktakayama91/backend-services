package com.bcp.falcon.reto.backendservices.security.expose.web;


import com.bcp.falcon.reto.backendservices.security.business.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@RestController
@RequestMapping("/api/falcon/services/security-services/otp")
public class OtpRestService {

    @Autowired
    private OtpService otpService;

    @RequestMapping(value = "/validate/{otp}", method = RequestMethod.GET)
    public boolean validateOtp(@PathVariable String otp) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        return otpService.validateOtp(name, otp);
    }

}
