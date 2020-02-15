package com.bcp.falcon.reto.backendservices.customer.business;

import java.util.Optional;

import com.bcp.falcon.reto.backendservices.customer.repository.CustomerRepository;
import com.bcp.falcon.reto.backendservices.customer.repository.model.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kei Takayama
 * Created on 2/15/20.
 */

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Long saveCustomer(CustomerModel customerModel) {
        CustomerModel savedCustomerModel = customerRepository.save(customerModel);
        return savedCustomerModel.getId();
    }

    public CustomerModel getCustomer(Long id) {
        CustomerModel customerModelResponse = new CustomerModel();
        Optional<CustomerModel> customerModel = customerRepository.findById (id);

        if (customerModel.isPresent()) {
            customerModelResponse = customerModel.get();
        } else {
//            ERROR
        }

        return customerModelResponse;
    }
}
