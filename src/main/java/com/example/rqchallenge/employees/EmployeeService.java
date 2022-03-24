package com.example.rqchallenge.employees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private static final String API_URL_BASE = "http://dummy.restapiexample.com/api/v1";
    private static final String ERR_MSG = "API " + API_URL_BASE + "'%s' API currently unavailable. Try again in a few seconds. Returned error message: '%s'";

    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(API_URL_BASE).build();
        logger.info("REST Client built.");
    }

    public List<EmployeeModel> fetchEmployees() throws ServiceUnavailableException {
        String uri = "/employees";
        DummyApiModel<List<EmployeeModel>> dummy;

        try {
            dummy = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<DummyApiModel<List<EmployeeModel>>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(String.format("URI=%s, errMsg=%s", uri, e.getMessage()), e);
            throw new ServiceUnavailableException(String.format(ERR_MSG, uri, e.getMessage()));
        }

        logger.debug("URI=%s, body=%s", uri, dummy);
        return dummy == null ? null : dummy.getEmployees();
    }

    public EmployeeModel fetchEmployee(int id) throws ServiceUnavailableException {

        if (id < 1) throw new IllegalArgumentException("'id' must be greater than 0");

        String uri = "/employee/" + id;
        DummyApiModel<EmployeeModel> dummy;
        try {
            dummy = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(String.format("URI=%s, errMsg=%s", uri, e.getMessage()), e);
            throw new ServiceUnavailableException(String.format(ERR_MSG, uri, e.getMessage()));
        }

        return dummy == null ? null : dummy.getEmployees();
    }

    public List<EmployeeModel> fetchEmployees(String name) throws ServiceUnavailableException {
        List<EmployeeModel> employees = fetchEmployees();
        employees.removeIf(s -> !s.getName().contains(name));
        return employees;
    }

    public List<String> fetchTopTenHighestEarningEmployeeNames() throws ServiceUnavailableException {
        List<EmployeeModel> employees = fetchEmployees();
        Comparator<EmployeeModel> employeeSalaryComparator = Comparator.comparingDouble(EmployeeModel::getSalary).reversed();
        Collections.sort(employees, employeeSalaryComparator);

        return employees.stream().limit(10).map(EmployeeModel::getName).collect(Collectors.toList());
    }

    public Integer fetchHighestSalaryOfEmployees() throws ServiceUnavailableException {
        List<EmployeeModel> employees = fetchEmployees();

        EmployeeModel employeeWithHighestSalary = employees.stream().max(Comparator.comparingDouble(EmployeeModel::getSalary)).orElseThrow(NoSuchElementException::new);

        // Salary is usually double, but the challenge asks for int.
        return (int) employeeWithHighestSalary.getSalary();
    }

    public EmployeeModel create(EmployeeModel employee) throws ServiceUnavailableException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DummyApiModel<EmployeeModel> dummy = new DummyApiModel<>(null, employee, null);
        HttpEntity<DummyApiModel> httpEntity = new HttpEntity<>(dummy, headers);

        DummyApiModel<EmployeeModel> dummyCreated;
        String uri = "/create";
        try {
            dummyCreated = restTemplate.exchange(uri, HttpMethod.POST, null, new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(String.format("URI=%s, errMsg=%s", uri, e.getMessage()), e);
            throw new ServiceUnavailableException(String.format(ERR_MSG, uri, e.getMessage()));
        }

        employee.setId(dummyCreated.getEmployees().getId());

        return employee;
    }

    public EmployeeModel removeEmployee(int id) throws ServiceUnavailableException {
        EmployeeModel employee = fetchEmployee(id);

        if (employee != null) {
            DummyApiModel<String> dummy;
            String uri = "/delete/" + id;
            try {
                dummy = restTemplate.exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<DummyApiModel<String>>() {
                }).getBody();
            } catch (Exception e) {
                logger.error(String.format("URI=%s, errMsg=%s", uri, e.getMessage()), e);
                throw new ServiceUnavailableException(String.format(ERR_MSG, uri, e.getMessage()));
            }

            if (dummy == null || !dummy.getStatus().equalsIgnoreCase("success")) {
                throw new IllegalStateException(String.format("{'status':'%s', 'message':'%s'}", dummy.getStatus(), dummy.getMessage()));
            }
        }

        return employee;
    }
}
