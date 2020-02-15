package com.bcp.falcon.reto.backendservices.security.business;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import com.bcp.falcon.reto.backendservices.security.repository.UserOtpRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserOtpModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private UserOtpRepository userOtpRepository;

    @Value("${otp.max.age}")
    private int OTP_MAX_AGE;


    @Override
    public String generateOtp(String username) {

        String otp = null;

        Optional<UserOtpModel> activeUserOtpModel = userOtpRepository.findByUsernameAndActive(username, true);

        if (activeUserOtpModel.isPresent()) {
            activeUserOtpModel.get().setActive(false);
            userOtpRepository.save(activeUserOtpModel.get());
        }

            int randomPin = (int) (Math.random() * 9000) + 1000;
            otp = String.valueOf(randomPin);

            UserOtpModel userOtpModel = new UserOtpModel();

            userOtpModel.setOtp(otp);
            userOtpModel.setUsername(username);
            userOtpModel.setMaxAge(OTP_MAX_AGE);
            userOtpModel.setCreationDate(new Timestamp(System.currentTimeMillis()));
            userOtpModel.setActive(true);
            userOtpRepository.save(userOtpModel);


        return otp;
    }

    @Override
    public boolean validateOtp(String username, String otp) {

        UserOtpModel userOtpModel = userOtpRepository.findByUsernameAndOtp(username, otp);

        if (userOtpModel == null) {
            return false;
        }

        if (userOtpModel.isActive()) {

            Timestamp initTime = userOtpModel.getCreationDate();
            int maxAge = userOtpModel.getMaxAge();
            LocalDateTime finalTime = initTime.toLocalDateTime().plusSeconds(maxAge);

            Timestamp maxTime = Timestamp.valueOf(finalTime);
            Timestamp actualTime = new Timestamp(System.currentTimeMillis());

            boolean active =  actualTime.before(maxTime);

            if (!active) {
                userOtpModel.setActive(false);
                userOtpRepository.save(userOtpModel);

                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }
}
