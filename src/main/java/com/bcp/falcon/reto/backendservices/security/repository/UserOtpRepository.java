package com.bcp.falcon.reto.backendservices.security.repository;

import java.util.Optional;

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

    UserOtpModel findByNotificationId(Long notificationId);

    Optional<UserOtpModel> findByUsernameAndActive(String username, boolean active);
}
