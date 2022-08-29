package com.markswell.service;

import com.markswell.model.Customer;
import com.markswell.resource.CustomerResource;
import lombok.RequiredArgsConstructor;
import com.markswell.mapper.CustomerMap;
import com.markswell.dto.CustomerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;
import org.springframework.data.domain.PageImpl;
import com.markswell.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markswell.repository.customer.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.time.LocalDate;
import java.lang.reflect.Field;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMap customerMap;
    private final CustomerRepository repository;

    public Page<CustomerResponse> findAll(Pageable page) {
        Page<Customer> customers = repository.findAll(page);
        validateEmpty(customers);
        return convertCustomerPageToCustomerResponsePage(page, customers);
    }

    public Page<CustomerResponse> findByName(String name, Pageable page) {
        Page<Customer> customers = repository.findByName(name, page);
        validateEmpty(customers);
        return convertCustomerPageToCustomerResponsePage(page, customers);
    }

    public Page<CustomerResponse> findByBorn(LocalDate born, Pageable page) {
        Page<Customer> customers = repository.findByBorn(born, page);
        validateEmpty(customers);
        return convertCustomerPageToCustomerResponsePage(page, customers);
    }

    @Transactional
    public CustomerResponse save(Customer customer) {
        Customer response = repository.save(customer);
        return getCustomerResponse(response);
    }

    @Transactional
    public CustomerResponse update(Long id, Customer customer) {
        Customer customerReturn = findOne(id);
        validateEmpty(customerReturn);
        BeanUtils.copyProperties(customer, customerReturn, "id");
        return getCustomerResponse(customerReturn);
    }

    @Transactional
    public CustomerResponse updatePatch(Long id, Map<String, Object> customer) {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer convertValue = objectMapper.convertValue(customer, Customer.class);

        Customer customerReturn = findOne(id);
        validateEmpty(customerReturn);
        customer.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Customer.class, key);
            field.setAccessible(true);
            Object fieldValue = ReflectionUtils.getField(field, convertValue);
            ReflectionUtils.setField(field, customerReturn, fieldValue);
        });

        return getCustomerResponse(customerReturn);
    }

    @Transactional
    public void delete(Long id) {
        var customer = findOne(id);
        repository.delete(customer);
    }

    public CustomerResponse findById(Long id) {
        Customer customer = findOne(id);
        validateEmpty(customer);
        return getCustomerResponse(customer);
    }

    private Customer findOne(long id) {
        Optional<Customer> optionalCustomer = repository.findById(id);
        if(optionalCustomer.isEmpty()) {
            throw new NotFoundException();
        }
        return optionalCustomer.get();
    }

    private PageImpl<CustomerResponse> convertCustomerPageToCustomerResponsePage(Pageable page, Page<Customer> customers) {
        List<CustomerResponse> customerResponses = customers.stream().map(c -> getCustomerResponse(c)).toList();
        return new PageImpl<>(customerResponses, page, customers.getTotalPages());
    }

    private static void validateEmpty(Page<Customer> customers) {
        if(Objects.isNull(customers) || customers.getContent().size() == 0) {
            throw new NotFoundException();
        }
    }

    private static void validateEmpty(Customer customerReturn) {
        if(Objects.isNull(customerReturn)) {
            throw new NotFoundException();
        }
    }

    private CustomerResponse getCustomerResponse(Customer c) {
        CustomerResponse customerResponse = customerMap.customerToCustomerResponse(c);
        Link link = linkTo(CustomerResource.class).slash(customerResponse.getId()).withSelfRel();
        customerResponse.add(link);
        return customerResponse;
    }

}
