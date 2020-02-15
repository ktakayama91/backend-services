package com.bcp.falcon.reto.backendservices.notification.repository;

import java.util.UUID;

import com.bcp.falcon.reto.backendservices.notification.repository.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

    NotificationModel findByCode(String code);
}
