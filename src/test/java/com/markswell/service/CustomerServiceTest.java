package com.markswell.service;

import org.junit.jupiter.api.Test;
import com.markswell.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import com.markswell.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    private Pageable pageable;
    private Customer customer;

    @BeforeEach
    private void init() {
        pageable = Pageable.ofSize(5);
        customer = Customer.builder().name("customer_teste").born(LocalDate.parse("2001-01-01")).build();
    }

    @Test
    @DisplayName("Test customerService.findAll method")
    public void findAllTest() {
        Page<CustomerResponse> responsePage = customerService.findAll(pageable);
        assertEquals(5, responsePage.getContent().size());
    }

    @Test
    @DisplayName("Test customerService.findById method")
    public void findByIdTest() {
        CustomerResponse customerResponse = customerService.findById(1l);
        assertEquals(1l, customerResponse.getId());
        assertEquals("customer_1", customerResponse.getName());
    }

    @Test
    @DisplayName("Test customerService.findByName complete name method")
    public void findByNameSpecificTest() {
        Page<CustomerResponse> customer2 = customerService.findByName("customer_2", pageable);
        assertEquals(1, customer2.getContent().size());
        assertEquals(2l, customer2.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Test customerService.findByName sliced name method")
    public void findByNameGenericTest() {
        Page<CustomerResponse> customer = customerService.findByName("customer", pageable);
        assertEquals(5, customer.getContent().size());
        assertEquals(1l, customer.getTotalPages());
    }

    @Test
    @DisplayName("Test customerService.findByBorn method")
    public void findByBornTest() {
        Page<CustomerResponse> customer2 = customerService.findByBorn(LocalDate.parse("2001-06-16"), pageable);
        assertEquals(1, customer2.getContent().size());
        assertEquals(9l, customer2.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Test customerService.save method")
    public void saveTest() {
        CustomerResponse customerResponse = customerService.save(customer);
        CustomerResponse customerServiceById = customerService.findById(customerResponse.getId());
        assertEquals(customerResponse, customerServiceById);
        assertEquals(10l, customerServiceById.getId());
    }

    @Test
    @DisplayName("Test customerService.update method")
    public void updateTest() {
        CustomerResponse update = customerService.update(1l, customer);
        CustomerResponse customerServiceById = customerService.findById(1l);
        assertEquals(customerServiceById.getName(), update.getName());
        assertEquals(customerServiceById.getBorn(), update.getBorn());
    }

    @Test
    @DisplayName("Test customerService.updatePatch method")
    public void updatePatchTest() {
        Map<String, Object> map = Map.of("name", "custumer_patch");
        CustomerResponse customerResponse = customerService.updatePatch(1l, map);
        CustomerResponse serviceById = customerService.findById(1l);
        assertEquals(customerResponse.getName(), serviceById.getName());
    }

    @Test
    @DisplayName("Test customerService.delete method")
    public void deleteTest() {
        customerService.delete(1l);
    }

}