package com.markswell.resource;

import org.junit.jupiter.api.Test;
import com.markswell.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import io.restassured.response.Response;
import com.markswell.dto.ResponseBodyDTO;
import org.junit.jupiter.api.DisplayName;
import com.markswell.dto.CustomerResponse;
import com.markswell.handle.ResponseError;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.http.ContentType.JSON;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerResourceTest {

    @LocalServerPort
    private int port;

    private Customer customer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        customer = Customer.builder()
                .name("customer_teste")
                .born(LocalDate.parse("2000-01-01"))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(ANY));
    }

    @Test
    @DisplayName("Test GET URL /api/customer")
    public void findAllStatusTest() {
        given()
                .basePath("/api/customer")
                .port(port)
                .accept(JSON)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Teste GET URL /api/customer?size=5&page=0")
    public void findAllSizeTest() throws JsonProcessingException {
        Response response = given()
                .basePath("/api/customer")
                .queryParam("size", 5)
                .queryParam("page", 0)
                .port(port)
                .accept(JSON)
                .when()
                .get()
                .then().extract().response();

        var customerResponse = objectMapper.readValue(response.getBody().asString(), ResponseBodyDTO.class);

        assertEquals(5, customerResponse.getContent().size());
        assertEquals("customer_1", customerResponse.getContent().get(0).getName());

    }

    @Test
    @DisplayName("Teste GET URL /api/customer?size=5&page=0")
    public void findByIdTest() throws JsonProcessingException {
        String response = given()
                .basePath("/api/customer/1")
                .queryParam("size", 5)
                .queryParam("page", 0)
                .port(port)
                .accept(JSON)
                .when()
                .get()
                .then()
                .extract()
                .asString();
        var customerResponse = objectMapper.readValue(response, CustomerResponse.class);
        assertEquals("customer_1", customerResponse.getName());
    }

    @Test
    @DisplayName("Teste POST URL /api/customer")
    public void saveTest() throws JsonProcessingException {
        String response = given()
                .port(port)
                .accept(JSON)
                .contentType(JSON)
                .body(customer)
                .when()
                .post("/api/customer")
                .then().statusCode(201).extract().asString();

        var customerResponse = objectMapper.readValue(response, CustomerResponse.class);
        assertEquals(10L, customerResponse.getId());

    }

    @Test
    @DisplayName("Teste PUT URL /api/customer/{id}")
    public void updateTest() throws JsonProcessingException {
        String response = given()
                .port(port)
                .accept(JSON)
                .contentType(JSON)
                .body(customer)
                .when()
                .put("/api/customer/2")
                .then().statusCode(201).extract().asString();

        var customerResponse = objectMapper.readValue(response, CustomerResponse.class);
        assertEquals(2L, customerResponse.getId());
        assertEquals("customer_teste", customerResponse.getName());
    }

    @Test
    @DisplayName("Teste PATCH URL /api/customer/{id}")
    public void patchTest() throws JsonProcessingException {
        Map<String, String> map = Map.of("name", "customer_patch");
        String response = given()
                                .port(port)
                                .accept(JSON)
                                .contentType(JSON)
                                .body(map)
                                .when()
                                .patch("/api/customer/2")
                                .then()
                                .statusCode(201)
                                .extract()
                                .asString();

        var customerResponse = objectMapper.readValue(response, CustomerResponse.class);
        assertEquals(2L, customerResponse.getId());
        assertEquals("customer_patch", customerResponse.getName());
    }

    @Test
    @DisplayName("Teste DELETE URL /api/customer/{id}")
    public void deleteRequestTest() {
             given()
                .basePath("/api/customer/1")
                .port(port)
                .accept(JSON)
                .when()
                .delete()
                .then().statusCode(204);
    }

    @Test
    @DisplayName("Teste DELETE URL /api/customer/{id}")
    public void deleteWithInexistentIdRequestTest() {
        given()
                .basePath("/api/customer/50")
                .port(port)
                .accept(JSON)
                .when()
                .delete()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Teste GET URL /api/customer/{id} with wrong param")
    public void wrongRequestTest() throws JsonProcessingException {
        String response = given()
                .basePath("/api/customer/abc")
                .queryParam("size", 5)
                .queryParam("page", 0)
                .port(port)
                .accept(JSON)
                .when()
                .get()
                .then().extract().asString();
        var customerResponse = objectMapper.readValue(response, ResponseError.class);

        assertEquals(400, customerResponse.getCode());
    }

}