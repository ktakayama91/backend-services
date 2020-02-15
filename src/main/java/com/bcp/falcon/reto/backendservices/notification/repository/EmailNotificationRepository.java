package com.bcp.falcon.reto.backendservices.notification.repository;

import java.util.Optional;

import com.bcp.falcon.reto.backendservices.notification.repository.model.EmailNotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotificationModel, Long> {

    Optional<EmailNotificationModel> findByCode(String code);
}
