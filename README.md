# Coding Challenge
> Original instructions moved to [README2.md](./README2.md)

This repository contains my resolution to a code challenge I participated in. The instructions on what has been asked can be found at [README2.md](./README2.md). In addition, below, I document how to start and use this application through the command line.

## Start it:

1. Clone the repository:
    ```
    git clone https://github.com/bambrozio/rq-challenge-application.git
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


### Outputs
#### Raw execution with outputs for reference purposes:

```
curl http://localhost:8080/api/v1/employees
[{"id":1,"employee_name":"Tiger Nixon","employee_salary":320800.0,"employee_age":61,"profile_image":""},{"id":2,"employee_name":"Garrett Winters","employee_salary":170750.0,"employee_age":63,"profile_image":""},...] 

curl http://localhost:8080/api/v1/employees/search/jen
[{"id":11,"employee_name":"Jena Gaines","employee_salary":90560.0,"employee_age":30,"profile_image":""},{"id":21,"employee_name":"Jenette Caldwell","employee_salary":345000.0,"employee_age":30,"profile_image":""}]

curl http://localhost:8080/api/v1/employee/2
{"id":2,"employee_name":"Garrett Winters","employee_salary":170750.0,"employee_age":63,"profile_image":""}     

curl http://localhost:8080/api/v1/employee/highestSalary
API http://dummy.restapiexample.com/api/v1'/employees' with HTTP method 'GET' currently unavailable. Try again in a few seconds. Returned error: '429 Too Many Requests: "{<LF>    "message": "Too Many Attempts."<LF>}"'%   

curl http://localhost:8080/api/v1/employee/highestSalary
725000   

curl http://localhost:8080/api/v1/employees/topTenHighestEarningEmployeeNames
["Paul Byrd","Yuri Berry","Charde Marshall","Cedric Kelly","Tatyana Fitzpatrick","Brielle Williamson","Jenette Caldwell","Quinn Flynn","Rhona Davidson","Tiger Nixon"]     

curl -H "Content-Type: application/json" -X POST -d '{"employee_name": "Bruno Ambrozio", "employee_age": 35, "employee_salary": 10000}' http://localhost:8080/api/v1/employee
{'status':'success','message':'Employ Bruno Ambrozio created! Assigned ID: 4852'}

curl -X DELETE http://localhost:8080/api/v1/employee/2
Employee 'Garrett Winters' delete
```

**Note:** Sometimes, the Dummy API is flooded with too many requests. When this is the case, you will experience the following return:
```
curl http://localhost:8080/api/v1/employees/topTenHighestEarningEmployeeNames
API http://dummy.restapiexample.com/api/v1'/employees' with HTTP method 'GET' currently unavailable. Try again in a few seconds. Returned error: '429 Too Many Requests: "{<LF>    "message": "Too Many Attempts."<LF>}"'%   
```
Therefore, as per returned message, try again in a few seconds.