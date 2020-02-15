package com.bcp.falcon.reto.backendservices.security.repository;

import com.bcp.falcon.reto.backendservices.security.repository.model.UserOtpModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Repository
public interface UserOtpRepository extends JpaRepository <UserOtpModel, Long> {

    UserOtpModel findByUsernameAndOtp(String username, String otp);
}
