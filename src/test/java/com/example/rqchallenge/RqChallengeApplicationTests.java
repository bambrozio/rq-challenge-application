package com.example.rqchallenge;

import com.example.rqchallenge.employees.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RqChallengeApplication }
 */
@SpringBootTest
class RqChallengeApplicationTests {

    @Autowired
    private EmployeeService service;

    @Test
    void contextLoads() throws Exception {
        assertThat(service).isNotNull();
    }
}
