package com.example.rqchallenge.employees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = false)
public class DummyApiModel<T> {

    // TODO: remove it.
    //    {"status":"success",
    //    "data":{"id":1,"employee_name":"Tiger Nixon","employee_salary":320800,"employee_age":61,"profile_image":""},
    //    "message":"Successfully! Record has been fetched."}

    private final String status;
    private final T employees;
    private final String message;

    public DummyApiModel(String status, @JsonProperty("data") T employees, String message) {
        this.status = status;
        this.employees = employees;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public T getEmployees() {
        return employees;
    }

    public String getMessage() {
        return message;
    }
}
