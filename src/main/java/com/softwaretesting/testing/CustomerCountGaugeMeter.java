package com.softwaretesting.testing;

import com.softwaretesting.testing.customerManagement.service.CustomerManagementService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CustomerCountGaugeMeter {
    final
    CustomerManagementService customerManagementService;


    public CustomerCountGaugeMeter(MeterRegistry meterRegistry, CustomerManagementService customerManagementService) {
        Gauge.builder("customer_count", fetchCustomerCount()).
                description("current number of customers in DB").
                register(meterRegistry);

        this.customerManagementService = customerManagementService;
    }

    public Supplier<Number> fetchCustomerCount() {
        return () -> customerManagementService.list().size();
    }

}
