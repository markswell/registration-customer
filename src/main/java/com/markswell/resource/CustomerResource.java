package com.markswell.resource;

import com.markswell.model.Customer;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import com.markswell.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import com.markswell.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.time.LocalDate;
import javax.websocket.server.PathParam;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
public class CustomerResource {

    private final CustomerService customerService;

    @GetMapping
    @Operation(description = "Find all customers by pages")
    public ResponseEntity<Page<CustomerResponse>> listAll(@PathParam("page") Integer page,
                                                          @PathParam("size") Integer size) {
        Page<CustomerResponse> responses = customerService.findAll(getPage(page, size));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(description = "Find a customer by his id")
    public ResponseEntity<CustomerResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(description = "Find all customers with his name matchs to pattern by pages")
    public ResponseEntity<Page<CustomerResponse>> findByName(@PathVariable String name,
                                                             @PathParam("page") @ApiParam(name = "page") Integer page,
                                                             @PathParam("size") @ApiParam(name = "page") Integer size) {
        return ResponseEntity.ok(customerService.findByName(name, getPage(page, size)));
    }

    @GetMapping("/born/{born}")
    @Operation(description = "Find all customers with his born day matchs to pattern by pages")
    public ResponseEntity<Page<CustomerResponse>> findByBorn(@PathVariable String born,
                                                             @PathParam("page") @ApiParam(name = "page") Integer page,
                                                             @PathParam("size") @ApiParam(name = "page") Integer size) {
        return ResponseEntity.ok(customerService.findByBorn(LocalDate.parse(born), getPage(page, size)));
    }

    @PostMapping
    @Operation(description = "Save a new customer")
    public ResponseEntity<CustomerResponse> save(@RequestBody Customer customer) {
         return ResponseEntity.status(CREATED).body(customerService.save(customer));
    }

    @PutMapping("/{id}")
    @Operation(description = "Update a customer")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @RequestBody Customer customer) {
        return ResponseEntity.status(CREATED).body(customerService.update(id, customer));
    }

    @PatchMapping("/{id}")
    @Operation(description = "Update one or more fields in a customer")
    public ResponseEntity<CustomerResponse> updatePatch(@PathVariable Long id, @RequestBody Map<String, Object> customer) {
        return ResponseEntity.status(CREATED).body(customerService.updatePatch(id, customer));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete a custumer by his id")
    public ResponseEntity<CustomerResponse> deletePatch(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    private static PageRequest getPage(Integer page, Integer size) {
        if(isNull(page) || isNull(page)) {
            return PageRequest.of(0, 20);
        }
        return PageRequest.of(page, size);
    }

}
