package com.bcp.falcon.reto.backendservices.security.business;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.bcp.falcon.reto.backendservices.security.repository.UserOtpRepository;
import com.bcp.falcon.reto.backendservices.security.repository.model.UserOtpModel;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @InjectMocks
    private OtpServiceImpl otpService;

    @Mock
    UserOtpRepository userOtpRepository;

    @Test
    public void generateOtpSuccessTest() {

        String otp = otpService.generateOtp("username");

        Assertions.assertNotNull(otp);
        Assertions.assertEquals(otp.length(), 4);

    }

    @Test
    public void validateOtpSuccessTest() {

        UserOtpModel userOtpModel = new UserOtpModel();
        userOtpModel.setCreationDate(new Timestamp(System.currentTimeMillis()));
        userOtpModel.setMaxAge(360);
        userOtpModel.setActive(true);

        Mockito.when(userOtpRepository.findByUsernameAndOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(userOtpModel);

        boolean response = otpService.validateOtp("username", "1234");

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response);
    }

    @Test
    public void validateOtpInvalidSuccessTest() {

        LocalDateTime tenMinutesAgo = new Timestamp(System.currentTimeMillis()).toLocalDateTime().minusMinutes(10L);
        Timestamp tenMinutesAgoT = Timestamp.valueOf(tenMinutesAgo);

        UserOtpModel userOtpModel = new UserOtpModel();
        userOtpModel.setCreationDate(tenMinutesAgoT);
        userOtpModel.setMaxAge(360);
        userOtpModel.setActive(true);

        Mockito.when(userOtpRepository.findByUsernameAndOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(userOtpModel);

        boolean response = otpService.validateOtp("username", "1234");

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response);
    }

}