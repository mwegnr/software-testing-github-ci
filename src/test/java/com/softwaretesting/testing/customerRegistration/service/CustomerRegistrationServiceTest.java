package com.softwaretesting.testing.customerRegistration.service;

import com.softwaretesting.testing.dao.CustomerRepository;
import com.softwaretesting.testing.exception.BadRequestException;
import com.softwaretesting.testing.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

class CustomerRegistrationServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRegistrationService customerRegistrationService;

    @Captor
    private ArgumentCaptor<String> phoneNumberCaptor;

    private AutoCloseable closeable;

    final Faker dataFaker = new Faker();

    private Customer getSampleCustomer() {
        return new Customer(
                0L, // will automatically be replaced by hibernate
                dataFaker.name().username(),
                dataFaker.name().fullName(),
                dataFaker.phoneNumber().phoneNumber());
    }

    @BeforeEach
    void setUp() {
        // replacement for MockitoAnnotations.initMocks(this) which is deprecated
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

//    Tests for
//    - new valid customer
//    - already registered customer (IllegalStateException)
//    - other customer existing with same phone number (BadRequestException)


    @Test
    @DisplayName("Inserting a new, valid customer")
    void registerValidCustomerTest() {
        final Customer testCustomer = getSampleCustomer();
        when(customerRepository.selectCustomerByPhoneNumber(testCustomer.getPhoneNumber())).thenReturn(Optional.empty());
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        final Customer registeredCustomer = customerRegistrationService.registerNewCustomer(testCustomer);
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());

        assertEquals(testCustomer, registeredCustomer);
        assertEquals(testCustomer.getPhoneNumber(), phoneNumberCaptor.getValue());
    }

    @Test
    @DisplayName("Inserting an already existing customer, should throw Exception")
    void insertAlreadyExistingCustomerExpectingExceptionTest() {
        final Customer testCustomer = getSampleCustomer();
        final String expectedMessage = "You are already registered";

        when(customerRepository.selectCustomerByPhoneNumber(testCustomer.getPhoneNumber())).thenReturn(Optional.of(testCustomer));

        final Exception caughtException = assertThrows(IllegalStateException.class,
                () -> customerRegistrationService.registerNewCustomer(testCustomer));
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());

        assertEquals(testCustomer.getPhoneNumber(), phoneNumberCaptor.getValue());
        assertEquals(expectedMessage, caughtException.getMessage());
    }

    @Test
    @DisplayName("Inserting a new customer, but one with the same number already exists, should throw Exception")
    void insertCustomerWithAlreadyTakenPhoneNumberExpectingException() {
        final Customer existingCustomer = getSampleCustomer();
        final Customer newCustomer = getSampleCustomer();
        final String expectedMessage = "Phone Number " + newCustomer.getPhoneNumber() + " taken";

        when(customerRepository.selectCustomerByPhoneNumber(newCustomer.getPhoneNumber())).thenReturn(Optional.of(existingCustomer));

        final Exception exception = assertThrows(BadRequestException.class, () -> customerRegistrationService.registerNewCustomer(newCustomer));
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());

        assertEquals(newCustomer.getPhoneNumber(), phoneNumberCaptor.getValue());
        assertEquals(expectedMessage, exception.getMessage());
    }
}