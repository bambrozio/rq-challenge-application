package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public interface IEmployeeController {

    /**
     * Retrieves all employees.
     *
     * @return A list of {@link EmployeeModel}.
     * @throws IOException
     */
    @GetMapping("/employees")
    ResponseEntity<List<EmployeeModel>> getAllEmployees() throws IOException;

    /**
     * Retrieves employees whose names contain a given string (case insensitive).
     *
     * @param searchString The given string is to be used on name filtering.
     * @return A list of {@link EmployeeModel}.
     */
    @GetMapping("/employees/search/{searchString}")
    ResponseEntity<List<EmployeeModel>> getEmployeesByNameSearch(@PathVariable String searchString);

    /**
     * Retrieves an employee by the given ID.
     *
     * @param id The ID to be searched.
     * @return A {@link EmployeeModel}.
     */
    @GetMapping("/employee/{id}")
    ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable String id);

    /**
     * Retrieves the highest salary among the employees.
     *
     * @return An {@link Integer} with the salary value.
     */
    @GetMapping("/employee/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    /**
     * Retrieves the top-10 employee names based on their salaries.
     *
     * @return A list of {@link EmployeeModel}.
     */
    @GetMapping("/employees/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    /**
     * Creates a new employee.
     *
     * @param employeeInput A map with the parameters to create a new employee.
     *                      The keys should match the {@link EmployeeModel} attributes.
     */
    @PostMapping("/employee")
    ResponseEntity<EmployeeModel> createEmployee(@RequestBody Map<String, Object> employeeInput);

    /**
     * Removes an employee.
     *
     * @param id The ID of the employee to be removed.
     */
    @DeleteMapping("/employee/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);
}
