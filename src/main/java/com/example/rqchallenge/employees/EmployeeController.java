package com.example.rqchallenge.employees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController implements IEmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ResponseEntity getAllEmployees() throws IOException {
        try {
            List<EmployeeModel> employees = employeeService.fetchEmployees();
            return employees == null || employees.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "No record found") : ResponseEntity.ok(employees);
        } catch (ServiceUnavailableException e) {
            return serviceUnavailableError(e);
        }
    }

    @Override
    public ResponseEntity getEmployeesByNameSearch(String searchString) {
        try {
            List<EmployeeModel> employees = employeeService.fetchEmployees(searchString);
            return employees == null || employees.isEmpty() ? notFound() : ResponseEntity.ok(employees);
        } catch (ServiceUnavailableException e) {
            return serviceUnavailableError(e);
        }
    }

    @Override
    public ResponseEntity getEmployeeById(String id) {
        try {
            EmployeeModel employee = employeeService.fetchEmployee(Integer.valueOf(id));
            return employee == null ? notFound() : ResponseEntity.ok(employee);
        } catch (IllegalArgumentException iae) {
            logger.warn(String.format("Invalid id=%s. Exception message=%s", id, iae.getMessage()));
            logger.debug(iae.getMessage(), iae);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'id' must be a valid positive integer number");
        } catch (ServiceUnavailableException e) {
            return serviceUnavailableError(e);
        }
    }

    @Override
    public ResponseEntity getHighestSalaryOfEmployees() {
        try {
            Integer highestSalary = employeeService.fetchHighestSalaryOfEmployees();
            return highestSalary == null ? notFound() : ResponseEntity.ok(highestSalary);
        } catch (ServiceUnavailableException e) {
            return serviceUnavailableError(e);
        }
    }

    @Override
    public ResponseEntity getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> employeeNames = employeeService.fetchTopNHighestEarningEmployeeNames(10);
            return employeeNames == null || employeeNames.isEmpty() ? notFound() : ResponseEntity.ok(employeeNames);
        } catch (ServiceUnavailableException e) {
            return serviceUnavailableError(e);
        }
    }

    @Override
    public ResponseEntity createEmployee(Map<String, Object> employeeInput) {
        EmployeeModel employee, createdEmployee;
        try {
            employee = new EmployeeModel(employeeInput);
            createdEmployee = employeeService.create(employee);
        } catch (IllegalArgumentException iae) {
            logger.warn(String.format("Invalid employeeInput=%s. Exception message=%s",
                    employeeInput, iae.getMessage()));
            logger.debug(iae.getMessage(), iae);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (ServiceUnavailableException sue) {
            return serviceUnavailableError(sue);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                String.format("{'status':'success','message':'Employ %s created! Assigned ID: %s'}",
                        createdEmployee.getName(), createdEmployee.getId()));
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {

        try {
            EmployeeModel employee = employeeService.removeEmployee(Integer.valueOf(id));
            return employee == null ? notFound() : ResponseEntity.ok(
                    String.format("Employee '%s' deleted", employee.getName()));
        } catch (IllegalArgumentException iae) {
            logger.warn(String.format("Invalid id=%s. Exception message=%s", id, iae.getMessage()));
            logger.debug(iae.getMessage(), iae);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'id' must be a valid positive integer number");
        } catch (ServiceUnavailableException e) {
            return serviceUnavailableError(e);
        }
    }

    private ResponseEntity notFound() {
        String msgBody = "No record found";
        logger.warn(msgBody);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msgBody);
    }

    private ResponseEntity<String> serviceUnavailableError(ServiceUnavailableException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }
}
