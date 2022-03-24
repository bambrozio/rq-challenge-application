package com.example.rqchallenge.employees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeModel {

    private static final String ERR_MSG = "Field '%s' must be informed with a valid %s";

    @JsonProperty("id")
    private int id;
    @JsonProperty("employee_name")
    private String name;
    @JsonProperty("employee_salary")
    private double salary;
    @JsonProperty("employee_age")
    private int age;
    @JsonProperty("profile_image")
    private String image;

    public EmployeeModel() {
    }

    public EmployeeModel(Map<String, Object> fields) {

        checkVals(fields);
        this.id = Integer.valueOf(String.valueOf(fields.getOrDefault("id", this.id)));
        this.name = (String) fields.get("employee_name");
        this.salary = Double.valueOf(String.valueOf(fields.get("employee_salary")));
        this.age = Integer.valueOf(String.valueOf(fields.get("employee_age")));
        this.image = (String) fields.getOrDefault("profile_image", "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // TODO: better to Spring Validation (MapBindingResult) instead.
    private void checkVals(Map<String, Object> fields) {

        if (!fields.containsKey("employee_name") || String.valueOf(fields.get("employee_name")).isEmpty()
                || String.valueOf(fields.get("employee_name")).isBlank()) {
            throw new IllegalArgumentException(String.format(ERR_MSG, "employee_name", "name"));
        }

        try {
            Double.parseDouble(String.valueOf(fields.get("employee_salary")));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ERR_MSG, "employee_salary", "monetary value"));
        }

        try {
            Integer.parseInt(String.valueOf(fields.get("employee_age")));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ERR_MSG, "employee_age", "positive numeric value"));
        }
    }

    @Override
    public String toString() {
        return "EmployeeModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                ", image='" + image + '\'' +
                '}';
    }
}
