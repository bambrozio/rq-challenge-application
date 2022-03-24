package com.example.rqchallenge.employees;

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

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ResponseEntity getAllEmployees() throws IOException {
        try {
            List<EmployeeModel> employees = employeeService.fetchEmployees();
            return employees == null || employees.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record found") : ResponseEntity.ok(employees);
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity getEmployeesByNameSearch(String searchString) {
        try {
            List<EmployeeModel> employees = employeeService.fetchEmployees(searchString);
            return employees == null || employees.isEmpty() ? notFound() : ResponseEntity.ok(employees);
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }

    }

    @Override
    public ResponseEntity getEmployeeById(String id) {
        //TODO: check if I want to use regex instead. Also, should I allow only positives?
        try {
            EmployeeModel employee = employeeService.fetchEmployee(Integer.valueOf(id));
            return employee == null ? notFound() : ResponseEntity.ok(employee);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'id' must be a valid positive integer number");
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity getHighestSalaryOfEmployees() {
        try {
            Integer highestSalary = employeeService.fetchHighestSalaryOfEmployees();
            return highestSalary == null ? notFound() : ResponseEntity.ok(highestSalary);
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> employeeNames = employeeService.fetchTopTenHighestEarningEmployeeNames();
            return employeeNames == null || employeeNames.isEmpty() ? notFound() : ResponseEntity.ok(employeeNames);
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity createEmployee(Map<String, Object> employeeInput) {
        EmployeeModel employee, createdEmployee = null;
        try {
            employee = new EmployeeModel(employeeInput);
            createdEmployee = employeeService.create(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceUnavailableException e2) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e2.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try {
            EmployeeModel employee = employeeService.removeEmployee(Integer.valueOf(id));
            return employee == null ? notFound() : ResponseEntity.ok(String.format("Employee '%s' deleted", employee.getName()));
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    private ResponseEntity notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record found");
    }
}
