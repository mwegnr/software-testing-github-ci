package com.softwaretesting.testing.dao;

import com.softwaretesting.testing.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;


// see https://howtodoinjava.com/spring-boot2/testing/spring-boot-2-junit-5/ -> 5. Unit testing DAO / Repository Layer
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {
    /* Typical Use-Cases for Customer Repository:
     * - [x] add customer to repository
     * - [x] add multiple customers to repository
     * - [x] list all customers in DB/repo
     * - [x] find user by ID
     * - [x] find multiple customers with list of IDs
     * - [x] check if customer exists by ID
     * - [x] count customers in DB
     * - [x] delete customer from DB/repo by ID
     * - [x] delete multiple customers with list of IDs
     * - [x] delete customer from DB/repo by object
     * - [x] delete multiple customers with list of objects
     * - [x] delete all customers from DB
     * - [x] find user by username
     * - [x] find user by phone number
     */

    @Autowired
    private CustomerRepository customerRepository;
    private final Faker dataFaker = new Faker();

    private Customer getSampleCustomer() {
        return new Customer(
                0L, // will automatically be replaced by hibernate
                dataFaker.name().username(),
                dataFaker.name().fullName(),
                dataFaker.phoneNumber().phoneNumber());
    }

    /*
     * Tested Methods
     * - CrudRepository.save(Customer)
     * - CrudRepository.findById(Long)
     */
    @Test
    public void addCustomerAndFindById() {
        final Customer insertedCustomer = customerRepository.save(getSampleCustomer());
        final Optional<Customer> customerById = customerRepository.findById(insertedCustomer.getId());

        assertTrue(customerById.isPresent());
        assertEquals(insertedCustomer, customerById.get());
    }

    /*
     * Tested Methods
     * - CrudRepository.save(Customer)
     * - CrudRepository.existsById(Long)
     * - CrudRepository.deleteById(Long)
     */
    @Test
    public void addCustomerCheckExistenceAndDeleteById() {
        final Customer insertedCustomer = customerRepository.save(getSampleCustomer());

        assertTrue(customerRepository.existsById(insertedCustomer.getId()));

        customerRepository.deleteById(insertedCustomer.getId());
        assertFalse(customerRepository.existsById(insertedCustomer.getId()));
    }

    /*
     * Tested Methods
     * - CrudRepository.save(Customer)
     * - CustomerRepository.findByUserName(String)
     */
    @Test
    public void addCustomerAndFindByName() {
        final String userName = dataFaker.name().username();
        final Customer customerWithUserName = getSampleCustomer();
        customerWithUserName.setUserName(userName);

        final Customer insertedCustomer = customerRepository.save(customerWithUserName);
        final Optional<Customer> customerByUserName = customerRepository.findByUserName(userName);

        assertTrue(customerByUserName.isPresent());
        assertEquals(insertedCustomer, customerByUserName.get());
    }

    /*
     * Tested Methods
     * - CrudRepository.save(Customer)
     * - CustomerRepository.selectCustomerByPhoneNumber(String)
     */
    @Test
    public void addCustomerAndFindByPhoneNumber() {
        final String phoneNumber = dataFaker.phoneNumber().phoneNumber();
        final Customer customerWithPhoneNumber = getSampleCustomer();
        customerWithPhoneNumber.setPhoneNumber(phoneNumber);

        final Customer insertedCustomer = customerRepository.save(customerWithPhoneNumber);
        final Optional<Customer> customerByPhoneNumber = customerRepository.selectCustomerByPhoneNumber(phoneNumber);

        assertTrue(customerByPhoneNumber.isPresent());
        assertEquals(insertedCustomer, customerByPhoneNumber.get());
    }

    /*
     * Tested Methods
     * - CrudRepository.saveAll(List<Customer>)
     * - CrudRepository.findAllById(List<Customer>)
     * - CrudRepository.deleteAllById(List<Customer>)
     * - CrudRepository.count()
     */
    @Test
    public void addListOfCustomersFindAndDeleteByIdList() {
        final List<Customer> customerList = List.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());
        final Iterable<Customer> insertedCustomerList = customerRepository.saveAll(customerList);
        final List<Long> insertedIds = StreamSupport
                .stream(insertedCustomerList.spliterator(), false)
                .map(Customer::getId).toList();

        final Iterable<Customer> customerListByIdList = customerRepository.findAllById(insertedIds);

        assertEquals(insertedCustomerList, customerListByIdList);

        customerRepository.deleteAllById(insertedIds);
        assertEquals(0, customerRepository.count());
    }

    /*
     * Tested Methods
     * - CrudRepository.saveAll(List<Customer>)
     * - CrudRepository.findAll()
     * - CrudRepository.delete(Customer)
     * - CrudRepository.count()
     * - CrudRepository.existsById(Long)
     */
    @Test
    public void addListOfCustomersAndDeleteOne() {
        final List<Customer> customerList = List.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());
        final Iterable<Customer> insertedCustomerIterable = customerRepository.saveAll(customerList);

        final Iterable<Customer> allCustomerIterable = customerRepository.findAll();

        assertEquals(insertedCustomerIterable, allCustomerIterable);


        final List<Customer> insertedCustomerList = StreamSupport.stream(insertedCustomerIterable.spliterator(), false).toList();
        final Customer customerToDelete = insertedCustomerList.get(0);
        final List<Customer> expectedRemainingCustomers = List.of(insertedCustomerList.get(1), insertedCustomerList.get(2));

        customerRepository.delete(customerToDelete);

        assertEquals(2, customerRepository.count());
        assertEquals(expectedRemainingCustomers, customerRepository.findAll());
        assertFalse(customerRepository.existsById(customerToDelete.getId()));
    }

    /*
     * Tested Methods
     * - CrudRepository.saveAll(List<Customer>)
     * - CrudRepository.findAll()
     * - CrudRepository.deleteAll(List<Customer>)
     * - CrudRepository.count()
     */
    @Test
    public void addAndDeleteListOfCustomers() {
        final List<Customer> customerList = List.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());
        final Iterable<Customer> insertedCustomerList = customerRepository.saveAll(customerList);

        final Iterable<Customer> allCustomerList = customerRepository.findAll();

        assertEquals(insertedCustomerList, allCustomerList);

        customerRepository.deleteAll(insertedCustomerList);
        assertEquals(0, customerRepository.count());
    }

    /*
     * Tested Methods
     * - CrudRepository.saveAll(List<Customer>)
     * - CrudRepository.findAll()
     * - CrudRepository.deleteAll()
     * - CrudRepository.count()
     */
    @Test
    public void addListOfCustomersAndDeleteAll() {
        final List<Customer> customerList = List.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());
        final Iterable<Customer> insertedCustomerList = customerRepository.saveAll(customerList);

        final Iterable<Customer> allCustomerList = customerRepository.findAll();

        assertEquals(insertedCustomerList, allCustomerList);

        customerRepository.deleteAll();
        assertEquals(0, customerRepository.count());
    }

    /*
     * Tested Methods
     * - CrudRepository.saveAll(List<Customer>)
     * - CrudRepository.findAll()
     */
    @Test
    public void addListOfCustomersAndCompareWithListOfAllCustomers() {
        final List<Customer> customerList = List.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());
        final Iterable<Customer> insertedCustomerList = customerRepository.saveAll(customerList);

        final Iterable<Customer> allCustomerList = customerRepository.findAll();

        assertEquals(insertedCustomerList, allCustomerList);
    }

    /*
     * Tested Methods
     * - CrudRepository.saveAll(List<Customer>)
     * - CrudRepository.count()
     */
    @Test
    public void addListOfCustomersAndCount() {
        final List<Customer> customerList = List.of(getSampleCustomer(), getSampleCustomer(), getSampleCustomer());
        customerRepository.saveAll(customerList);

        assertEquals(customerList.size(), customerRepository.count());
    }
}