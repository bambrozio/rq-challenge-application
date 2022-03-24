package com.example.rqchallenge.employees;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link IEmployeeController }
 */
@SpringBootTest
@WebMvcTest(IEmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeController controller;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllEmployees() {
    }

    @Test
    void getEmployeesByNameSearch() {
    }

    @Test
    void getEmployeeById() throws Exception {

//        EmployeeModel employ = new EmployeeModel();
//        employ.setId(1);
//        employ.setName("Tiger Nixon");
//        employ.setSalary(320800);
//        employ.setAge(61);
//        employ.setImage("");
//
//        when(controller.getEmployeeById("1")).thenReturn(ResponseEntity.ok(employ));
//        this.mockMvc.perform(get("/1")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("Tiger Nixon")));
    }


    @Test
    void getHighestSalaryOfEmployees() {
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() {
    }

    @Test
    void createEmployee() {
    }

    @Test
    void deleteEmployeeById() {
    }
}