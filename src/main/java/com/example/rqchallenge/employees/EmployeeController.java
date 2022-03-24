package com.example.rqchallenge.employees;

import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }

    @Override
    public ResponseEntity<EmployeeModel> getEmployeeById(String id) {
        EmployeeModel employee = employeeService.fetchEmployee(id);
        return employee == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return null;
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    @Override
    public ResponseEntity<EmployeeModel> createEmployee(Map<String, Object> employeeInput) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
}
