package com.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;
import com.test.model.Employee;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;


public class EmployeeControllerTest {
    private static EmployeeDTO employeeDTO1;
    private static EmployeeDTO employeeDTO2;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeController employeeController;


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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getAllEmployee").
                        contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).
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
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getAllEmployee").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(result -> result.getResolvedException().getClass().equals(ResponseStatusException.class)).andExpect(result -> result.getResolvedException().getMessage().contains("Failed to retrieve employees")).andReturn();
        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());


    }

    @Test
    public void testSaveEmployee() throws Exception {

        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(5L).name("Priyanka").age(27).active(true).designation("QA").phoneNumber(6762772677L).build();
        Mockito.when(employeeService.saveEmployee(employeeDTO)).thenReturn(employeeDTO);

        String content = objectWriter.writeValueAsString(employeeDTO);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/save").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

        ResultActions mvcResult = mockMvc.perform(mockRequest).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue())).andExpect(MockMvcResultMatchers.jsonPath("$.empId").exists()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.getName())).andExpect(MockMvcResultMatchers.jsonPath("$.designation").value(employeeDTO.getDesignation())).andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Priyanka")));


    }

    @Test
    public void testSaveEmployee_Failure() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                // Prepare the input data (you can set invalid data to trigger an exception)
                .name(null).build();

        Mockito.when(employeeService.saveEmployee(employeeDTO)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Mock the behavior of the employeeService.saveEmployee method to throw an exception
        //   Mockito.doThrow(new RuntimeException("Failed to save employee")).when(employeeService).saveEmployee(Mockito.any(EmployeeDTO.class));

        String content = objectWriter.writeValueAsString(employeeDTO);
        // Perform the HTTP POST request to /save and check the response status code
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/save").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(result -> result.getResolvedException().getClass().equals(ResponseStatusException.class)).andExpect(result -> result.getResolvedException().getMessage().contains("Failed to retrieve employees")).andReturn();
        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetDataByEmployeeId_Success() throws Exception {
        Long employeeId = 101L;
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(employeeId).name("Arun").age(28).build();
        Mockito.when(employeeService.getDataById(employeeDTO.getEmpId())).thenReturn(employeeDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getbyid/{employeeId}", employeeId).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.empId").value(employeeDTO.getEmpId())).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.getName()));

    }

    @Test
    public void testGetDataByEmployeeId_WhenEmployeeNotFound() throws Exception {
        // Prepare the input data (an employee ID that doesn't exist in the system)
        Long employeeId = 999L;

        // Mock the behavior of the employeeService.getDataById method to throw EmployeeNotFoundException
        Mockito.when(employeeService.getDataById(employeeId)).thenThrow(new EmployeeNotFoundException("Employee Not Found"));

        // Perform the HTTP GET request to /getbyid/{employeeId} and check the response status code
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getbyid/{employeeId}", employeeId).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetDataByEmployeeId_Failure() throws Exception {
        Long employeeId = 101L;
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(employeeId).name("Arun").age(28).build();
        Mockito.when(employeeService.getDataById(employeeDTO.getEmpId())).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getbyid/{employeeId}", employeeId).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(result -> result.getResolvedException().getClass().equals(ResponseStatusException.class)).andExpect(result -> result.getResolvedException().getMessage().contains("Failed to Get employee data")).andReturn();
        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetDataByName_Success() throws Exception {
        String employeeName = "Pramij";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L).name(employeeName).build();
        Mockito.when(employeeService.getDataByName(employeeDTO.getName())).thenReturn(employeeDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getbyname/{employee-name}", employeeName).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.getName())).andExpect(MockMvcResultMatchers.jsonPath("$.empId", is(123))).andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetDataByName_WhenEmployeeNotFound() throws Exception {
        String employeeName = "Raj";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L).name(employeeName).age(28).build();
        Mockito.when(employeeService.getDataByName(employeeDTO.getName())).thenThrow(new EmployeeNotFoundException("Employee not found"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getbyname/{employee-name}", employeeName).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetDataByName_Failure() throws Exception {
        String employeeName = "Rahul";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L).name(employeeName).age(25).build();

        Mockito.when(employeeService.getDataByName(employeeName)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getbyname/{employee-name}", employeeName).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateData() throws Exception {
        Long empId = 123L;
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(empId).name("Pramij").age(27).build();

        Mockito.when(employeeService.updateData(empId, employeeDTO)).thenReturn(employeeDTO);
        String content = objectWriter.writeValueAsString(employeeDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/update/{employeeId}", empId).contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.getName())).andExpect(MockMvcResultMatchers.jsonPath("$.empId", is(123))).andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateData_WhenEmployeeNotFound() throws Exception {
        Long employeeId = 123L;
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(employeeId).name("Priyanka").age(27).build();
        Mockito.when(employeeService.updateData(employeeId, employeeDTO)).thenThrow(new EmployeeNotFoundException("Employee Not Found"));

        String content = objectWriter.writeValueAsString(employeeDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/update/{employeeId}", employeeId).contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateData_Failure() throws Exception {
        Long employeeId = 123L;
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(employeeId).name("Sunil").age(27).build();

        Mockito.when(employeeService.updateData(employeeId, employeeDTO)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        String content = objectWriter.writeValueAsString(employeeDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/update/{employeeId}", employeeId).contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteData() throws Exception {
        Long empId = 123L;
        // Mock the employeeService's behavior
        Mockito.doNothing().when(employeeService).deleteEmployeeData(empId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/{employeeId}", empId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string("Data Deleted Successfully !!!"));

        Mockito.verify(employeeService, Mockito.times(1)).deleteEmployeeData(empId);

    }

    @Test
    public void testDeleteData_WhenEmployeeNotFound() throws Exception {
        Long empId = 123L;
        Mockito.doThrow(new EmployeeNotFoundException("Employee Not Found")).doNothing().when(employeeService).deleteEmployeeData(empId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/{employeeId}", empId)).andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(employeeService, Mockito.times(1)).deleteEmployeeData(empId);
    }

    @Test
    public void testDeleteData_WhenEmployeeInactive() throws Exception {
        Long empId = 123L;
        Mockito.doThrow(new EmployeeInactiveException("Employee is inactive")).doNothing().when(employeeService).deleteEmployeeData(empId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/{employeeId}", empId)).andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(employeeService, Mockito.times(1)).deleteEmployeeData(empId);
    }

    @Test
    public void testDeleteData_Failure() throws Exception {
        Long empId = 123L;
        Mockito.doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)).doNothing().when(employeeService).deleteEmployeeData(empId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/{employeeId}", empId)).andExpect(MockMvcResultMatchers.status().isInternalServerError());

        Mockito.verify(employeeService, Mockito.times(1)).deleteEmployeeData(empId);
    }

    @Test
    public void testSearchByAnyInput() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L).name("Pramij").age(29).active(true).designation("Developer").build();
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO1, employeeDTO2);
        Mockito.when(employeeService.getDataByAnyInput(employeeDTO)).thenReturn(employeeDTOList);

        String content = objectWriter.writeValueAsString(employeeDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/search").contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(employeeDTO.getName())).andExpect(MockMvcResultMatchers.jsonPath("$[0].empId", is(123))).andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testSearchByAnyInput_Failure() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L).name("Pramij").age(29).active(true).designation("Developer").build();
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO1, employeeDTO2);
        Mockito.when(employeeService.getDataByAnyInput(employeeDTO)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        String content = objectWriter.writeValueAsString(employeeDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/search").contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();
        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetEmployeesBySearchCriteria_success() throws Exception {
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);

        // Mock the behavior of the employeeService.findDataBySearchEmployee() method
        Mockito.when(employeeService.findDataBySearchEmployee(Mockito.any(Employee.class))).thenReturn(employeeDTOList);

        // Prepare the request body
        Employee employee = new Employee();
        employee.setEmpId(1L);
        employee.setName("Pramij");
        String content = objectWriter.writeValueAsString(employee);

        // Perform the POST request
     MvcResult mvcResult =   mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/searchbyquery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Pramij")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].empId", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Sunil"))).andReturn();

     Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
     // Verify the service method is called with the correct argument
        Mockito.verify(employeeService, Mockito.times(1)).findDataBySearchEmployee(Mockito.eq(employee));
    }

    @Test
    public void testGetEmployeesBySearchCriteria_whenEmployeeNotFound() throws Exception{
        Employee employee = Employee.builder().empId(121L)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, "Delhi", "441202", "UP", "India", null)).toList()).build();

        Mockito.when(employeeService.findDataBySearchEmployee(employee))
                .thenThrow(new EmployeeNotFoundException("Employee not found !!"));

        String content = objectWriter.writeValueAsString(employee);
      MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/searchbyquery").
                      contentType(MediaType.APPLICATION_JSON)
                      .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

      Assertions.assertEquals(404, mvcResult.getResponse().getStatus());

    }
    @Test
    public void testGetEmployeesBySearchCriteria_failure() throws Exception {
        Employee employee = Employee.builder().empId(121L)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, "Delhi", "441202", "UP", "India", null)).toList()).build();

       Mockito.when(employeeService.findDataBySearchEmployee(employee)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        String content = objectWriter.writeValueAsString(employee);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/searchbyquery")
                        .contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetEmployeeData() throws Exception{
        Long empId =123L;
        String pinCode = "441202";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(empId)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, "Delhi", pinCode, "UP", "India", null)).
                        toList()).build();

        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO);
        Mockito.when(employeeService.getDataByQuery(empId,pinCode)).thenReturn(employeeDTOS);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/searchbyquery2")
                        .param("employeeId",empId.toString())
                        .param("pinCode", pinCode)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empId",is(123)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(employeeDTO.getName()))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        Assertions.assertFalse(employeeDTOS.isEmpty());
    }

    @Test
    public void testGetEmployeeData_whenEmployeeNotFound() throws Exception{
        Long empId =123L;
        String pinCode = "441202";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(empId)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, "Delhi", pinCode, "UP", "India", null)).
                        toList()).build();
        Mockito.when(employeeService.getDataByQuery(empId,pinCode)).thenThrow(new EmployeeNotFoundException("Employee not found !!"));

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/searchbyquery2")
                .param("employeeId",employeeDTO.getEmpId().toString())
                .param("pinCode", pinCode).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

       Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetEmployeeData_failure() throws Exception{
        Long empId =123L;
        String pinCode = "441202";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(empId)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, "Delhi", pinCode, "UP", "India", null)).
                        toList()).build();
        Mockito.when(employeeService.getDataByQuery(empId,pinCode)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/searchbyquery2").param("employeeId",empId.toString())
                .param("pinCode", pinCode).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

       Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetByCity() throws Exception{
        String city = "Mumbai";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, city, "343434", "UP", "India", null)).
                        toList()).build();
        List<EmployeeDTO> employeeDTOS = Arrays.asList(employeeDTO,employeeDTO1);
        Mockito.when(employeeService.findEmployeesByCity(city)).thenReturn(employeeDTOS);

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getByCity/{city}",city)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empId").value(employeeDTO.getEmpId()))
                .andReturn();

       Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
       Assertions.assertFalse(employeeDTOS.isEmpty());
    }
    @Test
    public void testGetByCity_failure() throws Exception{
        String city = "Mumbai";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().empId(123L)
                .name("Pramij").age(29).designation("Software Eng")
                .phoneNumber(625662566L).salary(200000.0)
                .addresses(Stream.of(new Address(20L, "Delhi", "343434", "UP", "India", null)).
                        toList()).build();
        List<EmployeeDTO> employeeDTOS = Arrays.asList(employeeDTO,employeeDTO1);

        Mockito.when(employeeService.findEmployeesByCity(city)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

      MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getByCity/{city}",city)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

      Assertions.assertEquals(500, mvcResult.getResponse().getStatus());
    }
    }
