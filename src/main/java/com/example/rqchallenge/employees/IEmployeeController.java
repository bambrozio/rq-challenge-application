package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
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

    // Commented out because the challenge statement asks for a String and not a Employ object:
    @PostMapping("/employee")
    ResponseEntity<EmployeeModel> createEmployee(@RequestBody Map<String, Object> employeeInput);

//    // As per challenge request:
//    // output - string of the status (i.e. success) description - this should return a status of success or failed based on if an employee was created
//    @PostMapping("/employee")
//    ResponseEntity<String> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping("/employee/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);
}
