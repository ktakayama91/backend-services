package com.bcp.falcon.reto.backendservices.payment.repository;

import java.util.List;

import com.bcp.falcon.reto.backendservices.payment.repository.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {

    PaymentModel findByNotificationId(Long notificationId);

    List<PaymentModel> findAllByUserEmail(String userEmail);
}
