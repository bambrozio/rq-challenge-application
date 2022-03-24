package com.example.rqchallenge.employees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeService {

    public static final String API_URL_BASE = "http://dummy.restapiexample.com/api/v1";
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(API_URL_BASE).build();
        logger.info("REST Client built");
    }

    public List<EmployeeModel> fetchEmployees() {
        DummyApiModel<List<EmployeeModel>> dummy = restTemplate.exchange("/employees", HttpMethod.GET,
                null, new ParameterizedTypeReference<DummyApiModel<List<EmployeeModel>>>() {}).getBody();

        return dummy == null ? null : dummy.getEmployees();
    }

    public EmployeeModel fetchEmployee(String id) {

        // I wish I could use this, but "getForObject" does not support generics.
//        DummyApiModel<EmployeeModel> dummy = restTemplate.getForObject("/employee/" + id, DummyApiModel.class);

        DummyApiModel<EmployeeModel> dummy = restTemplate.exchange("/employee/" + id, HttpMethod.GET,
                null, new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {}).getBody();

        return dummy == null ? null : dummy.getEmployees();
    }
}
