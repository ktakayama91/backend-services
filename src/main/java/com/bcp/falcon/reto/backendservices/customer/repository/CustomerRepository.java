package com.bcp.falcon.reto.backendservices.customer.repository;

import java.util.Optional;

import com.bcp.falcon.reto.backendservices.customer.repository.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {

}
