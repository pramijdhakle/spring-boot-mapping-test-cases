package com.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.test.service.AddressService;
import com.test.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper= new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }


}
