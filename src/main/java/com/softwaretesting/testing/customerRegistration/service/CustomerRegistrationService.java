package com.softwaretesting.testing.customerRegistration.service;

import com.softwaretesting.testing.dao.CustomerRepository;
import com.softwaretesting.testing.exception.BadRequestException;
import com.softwaretesting.testing.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerRegistrationService {
    final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    public Customer registerNewCustomer(Customer customer) {
        Optional<Customer> existsPhoneNumber = customerRepository.selectCustomerByPhoneNumber(customer.getPhoneNumber());

        //TODO: Validate customer phone number

        if (existsPhoneNumber.isPresent()) {
            Customer existingCustomer = existsPhoneNumber.get();
            if (existingCustomer.getName().equals(customer.getName())) {
                logger.error("Customer already registered: %s".formatted(customer));
                throw new IllegalStateException("You are already registered");
            }
            logger.error("Phone Number %s taken".formatted(customer.getPhoneNumber()));
            throw new BadRequestException(
                    "Phone Number " + customer.getPhoneNumber() + " taken");
        }
        logger.info("Customer %s successfully registered");
        return customerRepository.save(customer);
    }
}






