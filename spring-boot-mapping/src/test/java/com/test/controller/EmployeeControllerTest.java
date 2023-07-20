package com.test.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;
import com.test.exception.GlobalExceptionHandling;
import com.test.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @MockBean
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandling())
                .build();
    }

    @Test
    void testSaveEmployee() throws Exception {
        // Prepare
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpId(123L);
        employeeDTO.setName("Pramij");
        employeeDTO.setDesignation("Dev");
        employeeDTO.setActive(true);
        employeeDTO.setAge(10);
        employeeDTO.setSalary(265626.8);
        employeeDTO.setPhoneNumber(2677267L);
     //   employeeDTO.setAddresses(null);
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);
String string= objectMapper.writeValueAsString(employeeDTO);
        // Perform the POST request
        MvcResult resultActions = mockMvc.perform(post("/api/v1/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect((ResultMatcher) status().isCreated()).andReturn();

        Assertions.assertEquals(201, resultActions.getResponse().getStatus());

        verify(employeeService, times(1)).saveEmployee(any(EmployeeDTO.class));
    }
    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
