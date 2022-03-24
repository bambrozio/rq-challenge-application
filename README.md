# Coding Challenge
> Original instructions moved to [README2.md](./README2.md)

This repository contains my resolution to a code challenge I participated in. The instructions on what has been asked can be found at [README2.md](./README2.md). In addition, below, I document how to start and use this application through the command line.

## Start it:

1. Clone the repository:
    ```
    git clone git@github.com:bambrozio/rq-challenge-application.git
    cd rq-challenge-application
    ```
2. Start the app:
    ```
    ./gradlew bootRun
    ```

## Use it:
### In another terminal instance, use the APIs:

1. Get all employees:
    ```
    curl http://localhost:8080/api/v1/employees
    ```
2. Get employees whose names contain a given string. For example: `jen`:
    ```
    curl http://localhost:8080/api/v1/employees/search/jen
    ```
3. Get employee by the given ID. For example: `2`:
    ```
    curl http://localhost:8080/api/v1/employee/2
    ```
4. Get the highest salary among the employees:
    ```
    curl http://localhost:8080/api/v1/employee/highestSalary
    ```
5. Get the top-10 employee names based on their salaries:
    ```
    curl http://localhost:8080/api/v1/employees/topTenHighestEarningEmployeeNames
    ```
6. Create a new employee:
    ```
    curl -H "Content-Type: application/json" -X POST -d '{"employee_name": "Bruno Ambrozio", "employee_age": 35, "employee_salary": 10000}' http://localhost:8080/api/v1/employee
    ```
7. Delete an employee:
    ```
    curl -X DELETE http://localhost:8080/api/v1/employee/2
    ```
