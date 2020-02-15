package com.bcp.falcon.reto.backendservices.security.business;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.bcp.falcon.reto.backendservices.security.repository.UserOtpRepository;
import com.bcp.falcon.reto.backendservices.security.repository.UserSessionRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserOtpModel;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserSessionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Service
public class OtpService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserOtpRepository userOtpRepository;


    public String generateOtp(String username) {
        int randomPin = (int) (Math.random() * 9000) + 1000;
        String otp = String.valueOf(randomPin);

        UserSessionModel userSessionModel = userSessionRepository.findByUsername(username);

        UserOtpModel userOtpModel = new UserOtpModel();

        userOtpModel.setOtp(otp);
        userOtpModel.setUsername(username);
        userOtpModel.setMaxAge(300);
        userOtpModel.setCreationDate(new Timestamp(System.currentTimeMillis()));

        userOtpRepository.save(userOtpModel);

        return otp;
    }

    public boolean validateOtp(String name, String otp) {

        UserOtpModel userOtpModel = userOtpRepository.findByUsernameAndOtp(name, otp);

        Timestamp initTime = userOtpModel.getCreationDate();
        int maxAge = userOtpModel.getMaxAge();
        LocalDateTime finalTime = initTime.toLocalDateTime().plusSeconds(maxAge);

        Timestamp maxTime = Timestamp.valueOf(finalTime);
        Timestamp actualTime = new Timestamp(System.currentTimeMillis());

        return actualTime.before(maxTime);
    }
}
