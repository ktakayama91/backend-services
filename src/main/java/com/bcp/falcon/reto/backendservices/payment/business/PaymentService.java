package com.bcp.falcon.reto.backendservices.payment.business;

import java.util.ArrayList;
import java.util.List;

import com.bcp.falcon.reto.backendservices.payment.repository.PaymentRepository;
import com.bcp.falcon.reto.backendservices.payment.repository.model.PaymentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<PaymentModel> getPayments(String userEmail) {
        return paymentRepository.findAllByUserEmail(userEmail);
    }


}
