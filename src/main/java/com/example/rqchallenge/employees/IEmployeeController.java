package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
//TODO: Use accepted headers for versioning instead (although for REST-API, URL versioning is fine).
@RequestMapping("/api/v1")
public interface IEmployeeController {

    @GetMapping("/employees")
    ResponseEntity<List<EmployeeModel>> getAllEmployees() throws IOException;

    @GetMapping("/employees/search/{searchString}")
    ResponseEntity<List<EmployeeModel>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/employee/{id}")
    ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable String id);

    @GetMapping("/employee/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/employees/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping("/employee")
    ResponseEntity<EmployeeModel> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping("/employee/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);
}
