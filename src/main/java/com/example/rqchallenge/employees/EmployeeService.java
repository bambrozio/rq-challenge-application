package com.example.rqchallenge.employees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    // TODO: Address the intermittent issue: "org.springframework.web.client.HttpClientErrorException$TooManyRequests: 429 Too Many Requests: "{<LF>    "message": "Too Many Attempts."<LF>}""
    // Perhaps adding retry & caching.
    public static final String API_URL_BASE = "http://dummy.restapiexample.com/api/v1";
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(API_URL_BASE).build();
        logger.info("REST Client built");
    }

    public List<EmployeeModel> fetchEmployees() {
        DummyApiModel<List<EmployeeModel>> dummy = restTemplate.exchange("/employees", HttpMethod.GET,
                null, new ParameterizedTypeReference<DummyApiModel<List<EmployeeModel>>>() {
                }).getBody();

        return dummy == null ? null : dummy.getEmployees();
    }

    public EmployeeModel fetchEmployee(int id) {

        // I wish I could use this, but "getForObject" does not support generics.
//        DummyApiModel<EmployeeModel> dummy = restTemplate.getForObject("/employee/" + id, DummyApiModel.class);

        DummyApiModel<EmployeeModel> dummy = restTemplate.exchange("/employee/" + id, HttpMethod.GET,
                null, new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {
                }).getBody();

        return dummy == null ? null : dummy.getEmployees();
    }

    public List<EmployeeModel> fetchEmployees(String name) {
        List<EmployeeModel> employees = fetchEmployees();
        employees.removeIf(s -> !s.getName().contains(name));
        return employees;
    }

    public List<String> fetchTopTenHighestEarningEmployeeNames() {
        List<EmployeeModel> employees = fetchEmployees();
        Comparator<EmployeeModel> employeeSalaryComparator = Comparator.comparingDouble(EmployeeModel::getSalary).reversed();
        Collections.sort(employees, employeeSalaryComparator);

        return employees.stream().limit(10).map(EmployeeModel::getName).collect(Collectors.toList());
    }

    public Integer fetchHighestSalaryOfEmployees() {
        List<EmployeeModel> employees = fetchEmployees();

        EmployeeModel employeeWithHighestSalary = employees
                .stream()
                .max(Comparator.comparingDouble(EmployeeModel::getSalary))
                .orElseThrow(NoSuchElementException::new);

        // Salary is usually double, but the challenge asks for int.
        return (int) employeeWithHighestSalary.getSalary();
    }

    public EmployeeModel create(EmployeeModel employee) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DummyApiModel<EmployeeModel> dummy = new DummyApiModel<>(null, employee, null);
        HttpEntity<DummyApiModel> httpEntity = new HttpEntity<>(dummy, headers);

//        DummyApiModel<EmployeeModel> dummyCreated = restTemplate.postForObject("/create", httpEntity, DummyApiModel.class);

        DummyApiModel<EmployeeModel> dummyCreated = restTemplate.exchange("/create", HttpMethod.POST,
                null, new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {
                }).getBody();

        employee.setId(dummyCreated.getEmployees().getId());

        return  employee;
    }

    public EmployeeModel removeEmployee(int id) {
        EmployeeModel employee = fetchEmployee(id);

        if(employee != null){
            DummyApiModel<String> dummy = restTemplate.exchange("/delete/" + id, HttpMethod.DELETE,
                    null, new ParameterizedTypeReference<DummyApiModel<String>>() {
                    }).getBody();


            if(dummy == null || !dummy.getStatus().equalsIgnoreCase("success")){
                throw new IllegalStateException(
                        String.format("{'status':'%s', 'message':'%s'}", dummy.getStatus(), dummy.getMessage()));
            }
        }

        return employee;
    }
}
