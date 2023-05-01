package com.softwaretesting.testing.validator;

import com.softwaretesting.testing.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerValidatorTest {
    final Faker dataFaker = new Faker();
    final CustomerValidator validator = new CustomerValidator();

    private Customer getSampleCustomer() {
        return new Customer(
                0L, // will automatically be replaced by hibernate
                dataFaker.name().username(),
                dataFaker.name().fullName(),
                dataFaker.phoneNumber().phoneNumber());
    }

    @Test
    void constructorTest() {
        assertNotNull(validator);
    }

    @Test
    void objectPresentTest() {
        final Customer sampleCustomer = getSampleCustomer();
        final Optional<Customer> existingCustomerOptional = Optional.of(sampleCustomer);

        assertDoesNotThrow(() ->
                validator.validate404(existingCustomerOptional, "[User-Name]", sampleCustomer.getUserName()));
    }

    @Test
    void objectNotPresentTest() {
        final Optional<Customer> emptyCustomerOptional = Optional.empty();
        // Message is not according to documentation, usually source code should be adopted, not test case
        final String expectedMessage = "404 NOT_FOUND \"java.util.Optional with [User-Name]'[Jacob]' does not exist.\"";

        final ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                validator.validate404(emptyCustomerOptional, "[User-Name]", "[Jacob]"));
        assertEquals(expectedMessage, exception.getMessage());
    }
}