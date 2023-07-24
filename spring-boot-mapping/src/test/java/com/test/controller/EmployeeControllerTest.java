package com.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dto.EmployeeDTO;
import com.test.model.Address;
import com.test.service.EmployeeService;
import lombok.SneakyThrows;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


public class EmployeeControllerTest {
    private static EmployeeDTO employeeDTO1;
    private static EmployeeDTO employeeDTO2;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeController employeeController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        loadEmployee();
    }

    public void loadEmployee() {
        employeeDTO1 = new EmployeeDTO(1L, "Pramij", 34, true, 1234567890L, "Eng", 727727.6, Stream.of(new Address(10L, "Mumbai", "440001", "Maharashtra", "India", null)).toList());
        employeeDTO2 = new EmployeeDTO(2L, "Sunil", 28, true, 8552987878L, "QA", 27727.6, Stream.of(new Address(20L, "Delhi", "441202", "UP", "India", null)).toList());
    }

    @SneakyThrows
    @Test
    public void testGetAllEmployees() {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO1);
        employeeDTOS.add(employeeDTO2);
        Mockito.when(employeeService.getAllEmployee()).thenReturn(employeeDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getAllEmployee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))// "$" says -> check the entire Json
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Sunil")));// here we check the value of 2nd[1] record, name=Sunil

         // Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

       /* MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getAllEmployee");
        ResultActions resultActions= mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status= response.getStatus();
        Assertions.assertEquals(200, status);*/
    }

    @SneakyThrows
    @Test
    public void testGetAllEmployees_WhenFailure() {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO1);
        employeeDTOS.add(employeeDTO2);
        // Assertions.assertThrows(ResponseStatusException.class, ()-> employeeController.getAllemployees());
        Mockito.when(employeeService.getAllEmployee()).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getAllEmployee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(result -> result.getResolvedException().getClass().
                        equals(ResponseStatusException.class))
                .andExpect(result -> result.getResolvedException().getMessage().contains("Failed to retrieve employees")).andReturn();
        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());


    }

    @Test
    public void testSaveEmployee() throws Exception {
        Mockito.when(employeeService.saveEmployee(employeeDTO1)).thenReturn(employeeDTO1);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/save")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        Assertions.assertEquals(201, mvcResult.getResponse().getStatus());
    }
}
