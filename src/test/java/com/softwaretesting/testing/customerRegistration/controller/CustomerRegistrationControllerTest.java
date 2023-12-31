package com.softwaretesting.testing.customerRegistration.controller;

import com.softwaretesting.testing.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String endpoint_base = "/api/v1/customer-registration";

    final Faker dataFaker = new Faker();

    private Customer getSampleCustomer() {
        return new Customer(
                0L, // will automatically be replaced by hibernate
                dataFaker.name().username(),
                dataFaker.name().fullName(),
                dataFaker.phoneNumber().phoneNumber());
    }

    private String getCustomerJson(final Customer customer) {
        return "{\"userName\": \"" + customer.getUserName() + "\"," +
                "\"name\": \"" + customer.getName() + "\"," +
                "\"phoneNumber\": \"" + customer.getPhoneNumber() + "\"}";
    }


    @Test
    @DisplayName("Successfully add a new Customer using the customer-registration endpoint")
    void createCustomerTest() throws Exception {
        final Customer customer = getSampleCustomer();

        mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(customer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getCustomerJson(customer)));
    }


    @Test
    @Disabled("This test fails due to missing exception handling in the code")
    @DisplayName("Unsuccessfully try to add same Customer twice")
    void createCustomerTwiceTest() throws Exception {
        final Customer customer = getSampleCustomer();

        // first, add a valid customer
        mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(customer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getCustomerJson(customer)));

        // then try to add the same customer again
        mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(customer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Unsuccessfully try to add a new Customer while one with the same number already exists")
    void createCustomerWithExistingPhoneNumberTest() throws Exception {
        final Customer firstCustomer = getSampleCustomer();
        final Customer secondCustomer = getSampleCustomer();
        secondCustomer.setPhoneNumber(firstCustomer.getPhoneNumber());

        // first, add a valid customer
        mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(firstCustomer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getCustomerJson(firstCustomer)));

        // then try to add a different customer using the same number
        mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(secondCustomer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test not allowed GET-Method")
    void notAllowedGetTest() throws Exception {
        mockMvc.perform(get(endpoint_base))
                .andExpect(status().isMethodNotAllowed());
    }


    @Test
    @DisplayName("Test not allowed DELETE-Method")
    void notAllowedDeleteMethodTest() throws Exception {
        mockMvc.perform(delete(endpoint_base))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Test not found list GET-Method")
    void unavailableListMethodTest() throws Exception {
        mockMvc.perform(get(endpoint_base + "/list"))
                .andExpect(status().isNotFound());
    }
}