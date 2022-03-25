package com.example.rqchallenge.employees;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String ERR_MSG = "API " + API_URL_BASE +
            "'%s' with HTTP method '%s' currently unavailable. Try again in a few seconds. Returned error: '%s'";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmployeeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(API_URL_BASE).build();
        logger.info("REST Client built.");
    }

    /**
     * Fetches all employees.
     *
     * @return A list with the found {@link EmployeeModel}. Null otherwise.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public List<EmployeeModel> fetchEmployees() throws ServiceUnavailableException {

        String uri = "/employees";
        DummyApiModel<List<EmployeeModel>> dummy;
        HttpMethod httpMethod = HttpMethod.GET;
        try {
            dummy = restTemplate.exchange(uri, httpMethod, null,
                    new ParameterizedTypeReference<DummyApiModel<List<EmployeeModel>>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(String.format("URI=%s, httpMethod=%s, errMsg=%s", uri, httpMethod, e.getMessage()), e);
            throw new ServiceUnavailableException(String.format(ERR_MSG, uri, httpMethod, e.getMessage()));
        }

        List<EmployeeModel> employees = dummy == null ? null : dummy.getEmployees();

        logger.info("Service URI={}, httpMethod={} executed. Number of employees returned={}",
                uri, httpMethod, employees == null ? 0 : employees.size());
        logger.debug("URI={}, httpMethod={}, body={}", uri, httpMethod, dummy);
        return employees;
    }

    /**
     * Fetches employee by the given ID.
     *
     * @param id The ID to be searched for.
     * @return The found {@link EmployeeModel}. Null otherwise.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public EmployeeModel fetchEmployee(int id) throws ServiceUnavailableException {

        if (id < 1) {
            throw new IllegalArgumentException("'id' must be greater than 0");
        }

        String uri = "/employee/" + id;
        DummyApiModel<EmployeeModel> dummy;
        HttpMethod httpMethod = HttpMethod.GET;
        try {
            dummy = restTemplate.exchange(uri, httpMethod, null,
                    new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {}).getBody();
        } catch (Exception e) {
            logger.error(String.format("URI=%s, httpMethod=%s, errMsg=%s", uri, httpMethod, e.getMessage()), e);
            throw new ServiceUnavailableException(String.format(ERR_MSG, uri, httpMethod, e.getMessage()));
        }

        logger.info("Service URI={}, httpMethod={} executed", uri, httpMethod);
        logger.debug("URI={}, httpMethod={}, body={}", uri, httpMethod, dummy);
        return dummy == null ? null : dummy.getEmployees();
    }

    /**
     * Fetches employees whose names contain the given string.
     *
     * @param name The given string to be used on the search filter.
     * @return A list with the found {@link EmployeeModel}.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public List<EmployeeModel> fetchEmployees(String name) throws ServiceUnavailableException {
        List<EmployeeModel> employees = fetchEmployees();
        employees.removeIf(s -> !s.getName().toLowerCase().contains(name.toLowerCase()));

        logger.info("Filtered names containing '{}'. Number of employees found={}",
                name, employees == null ? 0 : employees.size());
        logger.debug("Filtered string='{}'. Found employees={}", name, employees);
        return employees;
    }

    /**
     * Fetches the top-N employee names based on their salaries.
     *
     * @param n the amount of employees to be returned, based on their salaries.
     * @return A list of Strings with the found employees names.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public List<String> fetchTopNHighestEarningEmployeeNames(int n) throws ServiceUnavailableException {
        List<EmployeeModel> employees = fetchEmployees();
        Comparator<EmployeeModel> employeeSalaryComparator =
                Comparator.comparingDouble(EmployeeModel::getSalary).reversed();
        Collections.sort(employees, employeeSalaryComparator);
        List<String> ret = employees.stream().limit(n).map(EmployeeModel::getName).collect(Collectors.toList());

        logger.info("Filtered top-{} salaries.", n);
        logger.debug("Filtered top-{} salaries. Found employees={}", n, ret);
        return ret;
    }

    /**
     * Fetches the highest salary among the employees.
     *
     * @return An Integer with the higher salary found.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public Integer fetchHighestSalaryOfEmployees() throws ServiceUnavailableException {
        List<EmployeeModel> employees = fetchEmployees();

        EmployeeModel employeeWithHighestSalary =
                employees.stream().max(Comparator.comparingDouble(EmployeeModel::getSalary))
                        .orElseThrow(NoSuchElementException::new);

        // Salary is usually double, but the challenge asks for int, so casting it.
        int ret = (int) employeeWithHighestSalary.getSalary();

        logger.info("Filtered highest salary ***"); // sensitive information. Don't log in "INFO" level.
        logger.debug("Highest salary={}", ret);
        return ret;
    }

    /**
     * Creates a new employee.
     *
     * @param employee The given {@link EmployeeModel} to be created.
     * @return The created {@link EmployeeModel}, including its new ID.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public EmployeeModel create(EmployeeModel employee) throws ServiceUnavailableException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String uri = "/create";
        DummyApiModel<EmployeeModel> dummyCreated = null;
        String reqBody = null;
        HttpMethod httpMethod = HttpMethod.POST;
        try {
            reqBody = objectMapper.writeValueAsString(employee);
            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);
            dummyCreated = restTemplate.exchange(uri, httpMethod, request,
                    new ParameterizedTypeReference<DummyApiModel<EmployeeModel>>() {}).getBody();
        } catch (JsonProcessingException jpe) {
            logger.error("Fail to serialize given EmployeeModel into JSON", jpe);
        } catch (Exception e) {
            logger.error(String.format("URI=%s, httpMethod=%s, reqBody=%s, errMsg=%s",
                    uri, httpMethod, reqBody, e.getMessage()), e);
            throw new ServiceUnavailableException(String.format(ERR_MSG, uri, httpMethod, e.getMessage()));
        }

        employee.setId(dummyCreated.getEmployees().getId());

        logger.info("Service URI={}, httpMethod={} executed. Employee '{}' created. Given ID '{}'",
                uri, httpMethod, employee.getName(), employee.getId());
        logger.debug("URI={}, httpMethod={}, body={}, employee Response returned={}={}", uri, httpMethod, dummyCreated);
        return employee;
    }

    /**
     * Removes an employee.
     *
     * @param id The ID of the employee to be deleted.
     * @return The {@link EmployeeModel} just removed.
     * @throws ServiceUnavailableException When {@value #API_URL_BASE} is unavailable.
     */
    public EmployeeModel removeEmployee(int id) throws ServiceUnavailableException {
        EmployeeModel employee = fetchEmployee(id);

        DummyApiModel<String> dummy = null;
        String uri = "/delete/" + id;
        HttpMethod httpMethod = HttpMethod.DELETE;
        if (employee != null) {
            try {
                dummy = restTemplate.exchange(uri, httpMethod, null,
                        new ParameterizedTypeReference<DummyApiModel<String>>() {
                }).getBody();
            } catch (Exception e) {
                logger.error(String.format("URI=%s, httpMethod=%s errMsg=%s", uri, httpMethod, e.getMessage()), e);
                throw new ServiceUnavailableException(String.format(ERR_MSG, uri, httpMethod, e.getMessage()));
            }

            if (dummy == null || !dummy.getStatus().equalsIgnoreCase("success")) {
                throw new IllegalStateException(
                        String.format("{'status':'%s', 'message':'%s'}", dummy.getStatus(), dummy.getMessage()));
            }
        }

        logger.info("Service URI={}, httpMethod={}, executed", uri, httpMethod);
        logger.debug("URI={}, httpMethod={}, body={}, employee returned={}",
                uri, httpMethod, dummy, employee);
        return employee;
    }
}
