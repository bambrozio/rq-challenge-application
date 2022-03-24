package com.example.rqchallenge.employees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController implements IEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ResponseEntity<List<EmployeeModel>> getAllEmployees() throws IOException {
        List<EmployeeModel> employees = employeeService.fetchEmployees();
        return employees == null || employees.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<EmployeeModel>> getEmployeesByNameSearch(String searchString) {
        List<EmployeeModel> employees = employeeService.fetchEmployees(searchString);
        return employees == null || employees.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<EmployeeModel> getEmployeeById(String id) {
        //TODO: check if I want to use regex instead. Also, should I allow only positives?
        EmployeeModel employee = employeeService.fetchEmployee(Integer.valueOf(id));
        return employee == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highestSalary = employeeService.fetchHighestSalaryOfEmployees();
        return highestSalary == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> employeeNames = employeeService.fetchTopTenHighestEarningEmployeeNames();
        return employeeNames == null || employeeNames.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(employeeNames);
    }

    @Override
    public ResponseEntity createEmployee(Map<String, Object> employeeInput) {
        EmployeeModel employee, createdEmployee = null;
        try{
            employee = new EmployeeModel(employeeInput);
            createdEmployee = employeeService.create(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e2) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e2.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        EmployeeModel employee = employeeService.removeEmployee(Integer.valueOf(id));
        return employee == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(String.format("Employee '%s' deleted", employee.getName()));
    }
}
