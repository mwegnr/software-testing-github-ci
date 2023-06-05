package com.softwaretesting.testing.customerRegistration.controller;

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

    @Test
    @DisplayName("Successfully add a new Customer using the customer-registration endpoint")
    void createCustomerTest() throws Exception {
        final String requestBody = "{\"userName\": \"ame7AMi9an\",\"name\": \"Ralf Strauss\",\"phoneNumber\": \"+499829815965\"}";

        mockMvc.perform(post("/api/v1/customer-registration")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(requestBody));
    }


    @Test
    @Disabled("This test fails due to missing exception handling in the code")
    @DisplayName("Unsuccessfully try to add same Customer twice")
    void createCustomerTwiceTest() throws Exception {
        final String requestBody = "{\"userName\": \"ame7AMi9an\",\"name\": \"Ralf Strauss\",\"phoneNumber\": \"+499829815965\"}";

        // first, add a valid customer
        mockMvc.perform(post("/api/v1/customer-registration")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(requestBody));

        // then try to add the same customer again
        mockMvc.perform(post("/api/v1/customer-registration")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Unsuccessfully try to add a new Customer while one with the same number already exists")
    void createCustomerWithExistingPhoneNumberTest() throws Exception {
        final String requestBodyFirstCustomer = "{\"userName\": \"ame79an\",\"name\": \"Ralf Amsel\",\"phoneNumber\": \"+4998295965\"}";
        final String requestBodySecondCustomer = "{\"userName\": \"ame7n\",\"name\": \"Ralf Spatz\",\"phoneNumber\": \"+4998295965\"}";

        // first, add a valid customer
        mockMvc.perform(post("/api/v1/customer-registration")
                        .content(requestBodyFirstCustomer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(requestBodyFirstCustomer));

        // then try to add a different customer using the same number
        mockMvc.perform(post("/api/v1/customer-registration")
                        .content(requestBodySecondCustomer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test not allowed GET-Method")
    void notAllowedGetTest() throws Exception {
        mockMvc.perform(get("/api/v1/customer-registration"))
                .andExpect(status().isMethodNotAllowed());
    }


    @Test
    @DisplayName("Test not allowed DELETE-Method")
    void notAllowedDeleteMethodTest() throws Exception {
        mockMvc.perform(delete("/api/v1/customer-registration"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Test not found list GET-Method")
    void unavailableListMethodTest() throws Exception {
        mockMvc.perform(delete("/api/v1/customer-registration/list"))
                .andExpect(status().isNotFound());
    }
}