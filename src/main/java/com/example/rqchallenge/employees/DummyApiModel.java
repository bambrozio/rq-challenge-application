package com.example.rqchallenge.employees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = false)
public class DummyApiModel<T> {

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

    public T getEmployees() { return employees; }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DummyApiModel{" +
                "status='" + status + '\'' +
                ", employees=" + employees +
                ", message='" + message + '\'' +
                '}';
    }
}
