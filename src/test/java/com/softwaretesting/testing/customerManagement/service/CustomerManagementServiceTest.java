package com.softwaretesting.testing.customerManagement.service;

import com.softwaretesting.testing.dao.CustomerRepository;
import com.softwaretesting.testing.exception.BadRequestException;
import com.softwaretesting.testing.exception.CustomerNotFoundException;
import com.softwaretesting.testing.model.Customer;
import com.softwaretesting.testing.validator.CustomerValidator;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class CustomerManagementServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerValidator customerValidator;

    @InjectMocks
    private CustomerManagementServiceImp customerManagementService;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Captor
    private ArgumentCaptor<String> userNameCaptor;

    @Captor
    private ArgumentCaptor<String> phoneNumberCaptor;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Captor
    private ArgumentCaptor<List<Customer>> customerListCaptor;

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

    @Test
    @DisplayName("Searching successfully for a customer by ID")
    void findValidCustomerByIdTest() {
        final Customer expectedCustomer = getSampleCustomer();
        when(customerRepository.findById(expectedCustomer.getId())).thenReturn(Optional.of(expectedCustomer));

        final Customer actualCustomer = customerManagementService.findById(expectedCustomer.getId());
        then(customerRepository).should().findById(idCaptor.capture());

        assertEquals(expectedCustomer, actualCustomer);
        assertEquals(expectedCustomer.getId(), idCaptor.getValue());
        verify(customerRepository, times(1)).findById(expectedCustomer.getId());
        verify(customerValidator, times(1)).validate404(Optional.of(expectedCustomer), "id", String.valueOf(expectedCustomer.getId()));
    }

    @Test
    @DisplayName("Searching for a non-existent customer by ID and expecting exception")
    void findMissingCustomerByIdTest() {
        final Long idOfCustomerToDelete = getSampleCustomer().getId();
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [Id]'[" + idOfCustomerToDelete + "]' does not exist.\"";

        when(customerRepository.findById(idOfCustomerToDelete))
                .thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "java.util.Optional with [Id]'[" + idOfCustomerToDelete + "]' does not exist."))
                .when(customerValidator)
                .validate404(any(), anyString(), anyString());


        final Exception exception = assertThrows(ResponseStatusException.class, () -> customerManagementService.findById(idOfCustomerToDelete));
        then(customerRepository).should().findById(idCaptor.capture());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(idOfCustomerToDelete, idCaptor.getValue());
        verify(customerRepository, times(1)).findById(idOfCustomerToDelete);
        verify(customerValidator, times(1)).validate404(Optional.empty(), "id", String.valueOf(idOfCustomerToDelete));
    }

    @Test
    @DisplayName("Searching successfully for a customer by username")
    void findValidCustomerByUserNameTest() {
        final Customer expectedCustomer = getSampleCustomer();
        when(customerRepository.findByUserName(expectedCustomer.getUserName())).thenReturn(Optional.of(expectedCustomer));

        final Customer actualCustomer = customerManagementService.findByUserName(expectedCustomer.getUserName());
        then(customerRepository).should().findByUserName(userNameCaptor.capture());

        assertEquals(expectedCustomer, actualCustomer);
        assertEquals(expectedCustomer.getUserName(), userNameCaptor.getValue());
        verify(customerRepository, times(1)).findByUserName(expectedCustomer.getUserName());
        verify(customerValidator, times(1)).validate404(Optional.of(expectedCustomer), "User-Name", expectedCustomer.getUserName());
    }

    @Test
    @DisplayName("Searching for a non-existent customer by username and expecting exception")
    void findMissingCustomerByUserNameTest() {
        final String expectedUserName = getSampleCustomer().getUserName();
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [UserName]'[" + expectedUserName + "]' does not exist.\"";

        when(customerRepository.findByUserName(expectedUserName))
                .thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "java.util.Optional with [UserName]'[" + expectedUserName + "]' does not exist."))
                .when(customerValidator)
                .validate404(any(), anyString(), anyString());

        final Exception exception = assertThrows(ResponseStatusException.class, () -> customerManagementService.findByUserName(expectedUserName));
        then(customerRepository).should().findByUserName(userNameCaptor.capture());


        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedUserName, userNameCaptor.getValue());
        verify(customerRepository, times(1)).findByUserName(expectedUserName);
        verify(customerValidator, times(1)).validate404(Optional.empty(), "User-Name", expectedUserName);
    }

    @Test
    @DisplayName("Searching successfully for a customer by phone number")
    void findValidCustomerByPhoneNumberTest() {
        final Customer expectedCustomer = getSampleCustomer();
        when(customerRepository.selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber())).thenReturn(Optional.of(expectedCustomer));

        final Customer actualCustomer = customerManagementService.selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber());
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());

        assertEquals(expectedCustomer, actualCustomer);
        assertEquals(expectedCustomer.getPhoneNumber(), phoneNumberCaptor.getValue());
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(expectedCustomer.getPhoneNumber());
        verify(customerValidator, times(1)).validate404(Optional.of(expectedCustomer), "phone number", expectedCustomer.getPhoneNumber());
    }

    @Test
    @DisplayName("Searching for a non-existent customer by phone number and expecting exception")
    void searchMissingCustomerByPhoneNumberTest() {
        final String expectedPhoneNumber = getSampleCustomer().getPhoneNumber();
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [PhoneNumber]'[" + expectedPhoneNumber + "]' does not exist.\"";

        when(customerRepository.selectCustomerByPhoneNumber(expectedPhoneNumber))
                .thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "java.util.Optional with [PhoneNumber]'[" + expectedPhoneNumber + "]' does not exist."))
                .when(customerValidator)
                .validate404(any(), anyString(), anyString());

        final Exception exception = assertThrows(ResponseStatusException.class,
                () -> customerManagementService.selectCustomerByPhoneNumber(expectedPhoneNumber));
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedPhoneNumber, phoneNumberCaptor.getValue());
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(expectedPhoneNumber);
        verify(customerValidator, times(1)).validate404(Optional.empty(), "phone number", expectedPhoneNumber);
    }

    @Test
    @DisplayName("Successfully delete existing customer")
    void deleteExistingCustomerTest() {
        final Long idOfCustomerToDelete = getSampleCustomer().getId();

        when(customerRepository.existsById(idOfCustomerToDelete)).thenReturn(true);

        customerManagementService.delete(idOfCustomerToDelete);
        then(customerRepository).should().existsById(idCaptor.capture());

        assertEquals(idOfCustomerToDelete, idCaptor.getValue());
        verify(customerRepository, times(1)).existsById(idOfCustomerToDelete);
        verify(customerRepository, times(1)).deleteById(idOfCustomerToDelete);
    }

    @Test
    @DisplayName("Try unsuccessfully to delete non-existent customer")
    void deleteMissingCustomerTest() {
        final Long idOfCustomerToDelete = getSampleCustomer().getId();
        final String expectedMessage = "Customer with id " + idOfCustomerToDelete + " does not exists";

        when(customerRepository.existsById(idOfCustomerToDelete)).thenReturn(false);

        final Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerManagementService.delete(idOfCustomerToDelete));
        then(customerRepository).should().existsById(idCaptor.capture());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(idOfCustomerToDelete, idCaptor.getValue());
        verify(customerRepository, times(1)).existsById(idOfCustomerToDelete);
        verify(customerRepository, times(0)).deleteById(idOfCustomerToDelete);
    }

    @Test
    @DisplayName("Successfully add new customer")
    void addNewCustomerTest() {
        final Customer newCustomer = getSampleCustomer();

        when(customerRepository.selectCustomerByPhoneNumber(newCustomer.getPhoneNumber()))
                .thenReturn(Optional.empty());
        when(customerRepository.save(newCustomer)).thenReturn(newCustomer);

        final Customer addedCustomer = customerManagementService.addCustomer(newCustomer);
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());
        then(customerRepository).should().save(customerCaptor.capture());

        assertEquals(newCustomer, addedCustomer);
        assertEquals(newCustomer.getPhoneNumber(), phoneNumberCaptor.getValue());
        assertEquals(newCustomer, customerCaptor.getValue());
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(newCustomer.getPhoneNumber());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    @DisplayName("Try to unsuccessfully add a new customer with a taken phone number")
    void addNewCustomerWithExistingNumberTest() {
        final Customer newCustomer = getSampleCustomer();
        final String expectedMessage = "Phone Number " + newCustomer.getPhoneNumber() + " taken";

        when(customerRepository.selectCustomerByPhoneNumber(newCustomer.getPhoneNumber()))
                .thenReturn(Optional.of(getSampleCustomer()));


        final Exception exception = assertThrows(BadRequestException.class, () -> customerManagementService.addCustomer(newCustomer));
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumberCaptor.capture());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(newCustomer.getPhoneNumber(), phoneNumberCaptor.getValue());
        verify(customerRepository, times(1)).selectCustomerByPhoneNumber(newCustomer.getPhoneNumber());
        verify(customerRepository, times(0)).save(newCustomer);
    }

    @Test
    @DisplayName("List all customers")
    void listAllCustomersTest() {
        final Collection<Customer> expectedCustomerCollection = Set.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());

        when(customerRepository.findAll()).thenReturn(expectedCustomerCollection);

        final Collection<Customer> actualCustomers = customerManagementService.list();

        assertTrue(actualCustomers.containsAll(expectedCustomerCollection));
        assertTrue(expectedCustomerCollection.containsAll(actualCustomers));
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Save multiple new customers")
    void saveListOfCustomers() {
        final List<Customer> expectedCustomers = Arrays.asList(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());

        when(customerRepository.saveAll(expectedCustomers)).thenReturn(expectedCustomers);

        final Collection<Customer> actualCustomers = customerManagementService.saveAll(expectedCustomers);
        then(customerRepository).should().saveAll(customerListCaptor.capture());

        assertTrue(actualCustomers.containsAll(expectedCustomers));
        assertTrue(expectedCustomers.containsAll(actualCustomers));
        assertEquals(expectedCustomers, customerListCaptor.getValue());
        verify(customerRepository, times(1)).saveAll(expectedCustomers);
    }
}