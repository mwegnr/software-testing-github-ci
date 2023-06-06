package com.softwaretesting.testing.customerManagement.controller;

import com.softwaretesting.testing.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String endpoint_base = "/api/v1/customers";

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
        final String requestBody = getCustomerJson(getSampleCustomer());

        mockMvc.perform(post(endpoint_base)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(requestBody));
    }


    @Test
    @DisplayName("Unsuccessfully try to add same Customer twice")
    void createCustomerTwiceTest() throws Exception {
        final String requestBody = getCustomerJson(getSampleCustomer());

        // first, add a valid customer
        mockMvc.perform(post(endpoint_base)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(requestBody));

        // then try to add the same customer again
        mockMvc.perform(post(endpoint_base)
                        .content(requestBody)
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
    @DisplayName("Test GET with customer ID.")
    void getCustomerByIdTest() throws Exception {
        final Customer customer = getSampleCustomer();
        final Pattern idRegex = Pattern.compile("\"id\":(\\d{1,5})");

        final MvcResult result = mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(customer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getCustomerJson(customer)))
                .andReturn();

        final String response = result.getResponse().getContentAsString();
        final Matcher idMatcher = idRegex.matcher(response);
        if (idMatcher.find()) {
            final String id = idMatcher.group(1);

            mockMvc.perform(get(endpoint_base + "/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(getCustomerJson(customer)));
        } else {
            fail();
        }
    }

    @Test
    @DisplayName("Test GET without ID")
    void notAllowedGetTest() throws Exception {
        mockMvc.perform(get(endpoint_base))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Test GET with non-existent ID")
    void getNonExistentCustomerTest() throws Exception {
        // not sure if -1 is a good test value here,
        // one could argue that negative values should cause a bad request (400) instead of not found (404)
        mockMvc.perform(get(endpoint_base + "/{id}", "-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test DELETE with customer ID. Only works if createCustomerTest() was already executed.")
    void deleteCustomerByIdTest() throws Exception {
        final Customer customer = getSampleCustomer();
        final Pattern idRegex = Pattern.compile("\"id\":(\\d{1,5})");

        final MvcResult result = mockMvc.perform(post(endpoint_base)
                        .content(getCustomerJson(customer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getCustomerJson(customer)))
                .andReturn();

        final String response = result.getResponse().getContentAsString();
        final Matcher idMatcher = idRegex.matcher(response);
        if (idMatcher.find()) {
            final String id = idMatcher.group(1);
            mockMvc.perform(delete(endpoint_base + "/{id}", id))
                    .andExpect(status().isOk());
        } else {
            fail();
        }
    }

    @Test
    @DisplayName("Test not allowed DELETE-Method")
    void notAllowedDeleteMethodTest() throws Exception {
        mockMvc.perform(delete(endpoint_base))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Test DELETE with non-existent ID")
    void deleteNonExistentCustomerTest() throws Exception {
        // not sure if -1 is a good test value here,
        // one could argue that negative values should cause a bad request (400) instead of not found (404)
        mockMvc.perform(delete(endpoint_base + "/{id}", "-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test list GET-Method")
    void getListTest() throws Exception {
        // TODO: check response body somehow
        mockMvc.perform(get(endpoint_base + "/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}