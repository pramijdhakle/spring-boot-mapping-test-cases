package com.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.test.dto.AddressDTO;
import com.test.exception.EmployeeNotFoundException;
import com.test.service.AddressService;
import com.test.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AddressController addressController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper= new ObjectMapper();
    @Autowired
    private ObjectWriter objectWriter= objectMapper.writer();

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    public void testAddNewAddress_success() throws Exception {
        Long empId = 123L;
        AddressDTO addressDTO= AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();

        Mockito.when(addressService.add(empId, addressDTO)).thenReturn(addressDTO);

        String content = objectWriter.writeValueAsString(addressDTO);

     MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/employee/{empId}/save",empId)
                     .contentType(MediaType.APPLICATION_JSON)
                .content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(100))).andReturn();

        Assertions.assertEquals(201, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddNewAddress_whenEmployeeNotFound() throws Exception{
        Long empId = 123L;
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();

        Mockito.when(addressService.add(empId, addressDTO)).thenThrow(new EmployeeNotFoundException("Employee not found"));

        String content = objectWriter.writeValueAsString(addressDTO);

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/employee/{empId}/save", empId).
                       contentType(MediaType.APPLICATION_JSON)
                .content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

       Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddNewAddress_Failure() throws Exception {
        Long empId = 123L;
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();

        Mockito.when(addressService.add(empId, addressDTO)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        String content = objectWriter.writeValueAsString(addressDTO);
       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/employee/{empId}/save", empId).contentType(MediaType.APPLICATION_JSON)
                .content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

       Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }
}
