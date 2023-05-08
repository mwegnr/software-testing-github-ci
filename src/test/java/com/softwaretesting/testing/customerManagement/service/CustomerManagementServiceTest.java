package com.softwaretesting.testing.customerManagement.service;

import com.softwaretesting.testing.dao.CustomerRepository;
import com.softwaretesting.testing.exception.BadRequestException;
import com.softwaretesting.testing.exception.CustomerNotFoundException;
import com.softwaretesting.testing.model.Customer;
import com.softwaretesting.testing.validator.CustomerValidator;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerManagementServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerValidator customerValidator;

    @InjectMocks
    private CustomerManagementServiceImp customerManagementService;

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
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Searching successfully for a customer by ID")
    public void findValidCustomerByIdTest() {
        final Customer expectedCustomer = getSampleCustomer();
        when(customerRepository.findById(expectedCustomer.getId())).thenReturn(Optional.of(expectedCustomer));

        final Customer actualCustomer = customerManagementService.findById(expectedCustomer.getId());

        assertEquals(expectedCustomer, actualCustomer);
        verify(customerRepository, times(1)).findById(expectedCustomer.getId());
        verify(customerValidator, times(1)).validate404(Optional.of(expectedCustomer), "id", String.valueOf(expectedCustomer.getId()));
    }

    @Test
    @DisplayName("Searching for a non-existent customer by ID and expecting exception")
    public void findMissingCustomerByIdTest() {
        final Customer expectedCustomer = getSampleCustomer();
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [Id]'[" + expectedCustomer.getId() + "]' does not exist.\"";

        when(customerRepository.findById(expectedCustomer.getId()))
                .thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "java.util.Optional with [Id]'[" + expectedCustomer.getId() + "]' does not exist."))
                .when(customerValidator)
                .validate404(any(), anyString(), anyString());


        final Exception exception = assertThrows(ResponseStatusException.class,
                () -> customerManagementService.findById(expectedCustomer.getId()));

        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, times(1)).findById(expectedCustomer.getId());
        verify(customerValidator, times(1)).validate404(Optional.empty(), "id", String.valueOf(expectedCustomer.getId()));
    }

    @Test
    @DisplayName("Searching successfully for a customer by username")
    public void findValidCustomerByUserNameTest() {
        final Customer expectedCustomer = getSampleCustomer();
        when(customerRepository.findByUserName(expectedCustomer.getUserName())).thenReturn(Optional.of(expectedCustomer));

        final Customer actualCustomer = customerManagementService.findByUserName(expectedCustomer.getUserName());

        assertEquals(expectedCustomer, actualCustomer);
        verify(customerRepository, times(1)).findByUserName(expectedCustomer.getUserName());
        verify(customerValidator, times(1)).validate404(Optional.of(expectedCustomer), "User-Name", expectedCustomer.getUserName());
    }

    @Test
    @DisplayName("Searching for a non-existent customer by username and expecting exception")
    public void findMissingCustomerByUserNameTest() {
        final Customer expectedCustomer = getSampleCustomer();
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [UserName]'[" + expectedCustomer.getUserName() + "]' does not exist.\"";

        when(customerRepository.findByUserName(expectedCustomer.getUserName()))
                .thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "java.util.Optional with [UserName]'[" + expectedCustomer.getUserName() + "]' does not exist."))
                .when(customerValidator)
                .validate404(any(), anyString(), anyString());

        final Exception exception = assertThrows(ResponseStatusException.class,
                () -> customerManagementService.findByUserName(expectedCustomer.getUserName()));

        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, times(1)).findByUserName(expectedCustomer.getUserName());
        verify(customerValidator, times(1)).validate404(Optional.empty(), "User-Name", expectedCustomer.getUserName());
    }

    @Test
    @DisplayName("Searching successfully for a customer by phone number")
    public void findValidCustomerByPhoneNumberTest() {
        final Customer expectedCustomer = getSampleCustomer();
        when(customerRepository.selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber())).thenReturn(Optional.of(expectedCustomer));

        final Customer actualCustomer = customerManagementService.selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber());

        assertEquals(expectedCustomer, actualCustomer);
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber());
        verify(customerValidator, times(1)).validate404(Optional.of(expectedCustomer), "phone number", expectedCustomer.getPhoneNumber());
    }

    @Test
    @DisplayName("Searching for a non-existent customer by phone number and expecting exception")
    public void findMissingCustomerByPhoneNumberTest() {
        final Customer expectedCustomer = getSampleCustomer();
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [PhoneNumber]'[" + expectedCustomer.getPhoneNumber() + "]' does not exist.\"";

        when(customerRepository.selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber()))
                .thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "java.util.Optional with [PhoneNumber]'[" + expectedCustomer.getPhoneNumber() + "]' does not exist."))
                .when(customerValidator)
                .validate404(any(), anyString(), anyString());

        final Exception exception = assertThrows(ResponseStatusException.class,
                () -> customerManagementService.selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber()));

        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber());
        verify(customerValidator, times(1)).validate404(Optional.empty(), "phone number", expectedCustomer.getPhoneNumber());
    }

    @Test
    @DisplayName("Successfully delete existing customer")
    public void deleteExistingCustomerTest() {
        final Customer customerToDelete = getSampleCustomer();

        when(customerRepository.existsById(customerToDelete.getId())).thenReturn(true);

        customerManagementService.delete(customerToDelete.getId());
        verify(customerRepository, times(1)).existsById(customerToDelete.getId());
        verify(customerRepository, times(1)).deleteById(customerToDelete.getId());
    }

    @Test
    @DisplayName("Try unsuccessfully to delete non-existent customer")
    public void deleteMissingCustomerTest() {
        final Customer customerToDelete = getSampleCustomer();
        final String expectedMessage = "Customer with id " + customerToDelete.getId() + " does not exists";

        when(customerRepository.existsById(customerToDelete.getId())).thenReturn(false);

        final Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerManagementService.delete(customerToDelete.getId()));
        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, times(1)).existsById(customerToDelete.getId());
        verify(customerRepository, times(0)).deleteById(customerToDelete.getId());
    }

    @Test
    @DisplayName("Successfully add new customer")
    public void addNewCustomerTest() {
        final Customer newCustomer = getSampleCustomer();

        when(customerRepository.selectCustomerByPhoneNumber(newCustomer.getPhoneNumber()))
                .thenReturn(Optional.empty());
        when(customerRepository.save(newCustomer)).thenReturn(newCustomer);

        assertEquals(newCustomer, customerManagementService.addCustomer(newCustomer));
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(newCustomer.getPhoneNumber());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    @DisplayName("Try to unsuccessfully add a new customer with a taken phone number")
    public void addNewCustomerWithExistingNumberTest() {
        final Customer newCustomer = getSampleCustomer();
        final String expectedMessage = "Phone Number " + newCustomer.getPhoneNumber() + " taken";

        when(customerRepository.selectCustomerByPhoneNumber(newCustomer.getPhoneNumber()))
                .thenReturn(Optional.of(getSampleCustomer()));


        final Exception exception = assertThrows(BadRequestException.class, () -> customerManagementService.addCustomer(newCustomer));
        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(newCustomer.getPhoneNumber());
        verify(customerRepository, times(0)).save(newCustomer);
    }

    @Test
    @DisplayName("List all customers")
    public void listAllCustomersTest() {
        final Collection<Customer> expectedCustomerCollection = Set.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());

        when(customerRepository.findAll()).thenReturn(expectedCustomerCollection);

        final Collection<Customer> actualCustomers = customerManagementService.list();

        assertTrue(actualCustomers.containsAll(expectedCustomerCollection));
        assertTrue(expectedCustomerCollection.containsAll(actualCustomers));
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Save multiple new customers")
    @Disabled
    public void saveListOfCustomers() {
        fail(); // TODO
    }

}