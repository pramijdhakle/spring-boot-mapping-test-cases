package com.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.test.dto.AddressDTO;
import com.test.exception.AddressNotFoundException;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class AddressControllerTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private final ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private AddressService addressService;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private AddressController addressController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    public void testAddNewAddress_success() throws Exception {
        Long empId = 123L;
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();

        Mockito.when(addressService.add(empId, addressDTO)).thenReturn(addressDTO);

        String content = objectWriter.writeValueAsString(addressDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/employee/{empId}/save", empId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(100))).andReturn();

        Assertions.assertEquals(201, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddNewAddress_whenEmployeeNotFound() throws Exception {
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

    @Test
    public void testGetAllAddresses() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();
        AddressDTO addressDTO1 = AddressDTO.builder().id(101L).city("Mumbai").state("MH").country("India").pinCode("441001").build();
        List<AddressDTO> addressDTOList = Arrays.asList(addressDTO, addressDTO1);

        Mockito.when(addressService.addressDtoList()).thenReturn(addressDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/getAddress").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city", is("Mumbai")))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());


    }

    @Test
    public void testGetAllAddresses_whenAddressNotFound() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();
        AddressDTO addressDTO1 = AddressDTO.builder().id(101L).city("Mumbai").state("MH").country("India").pinCode("441001").build();
        List<AddressDTO> addressDTOList = Arrays.asList(addressDTO, addressDTO1);

        Mockito.when(addressService.addressDtoList()).thenThrow(new AddressNotFoundException("No addresses found."));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/getAddress").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAllAddresses_Failure() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();
        AddressDTO addressDTO1 = AddressDTO.builder().id(101L).city("Mumbai").state("MH").country("India").pinCode("441001").build();
        List<AddressDTO> addressDTOList = Arrays.asList(addressDTO, addressDTO1);

        Mockito.when(addressService.addressDtoList()).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/getAddress").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddressResponse_success() throws Exception {
        Long employeeId = 123L;
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();
        AddressDTO addressDTO1 = AddressDTO.builder().id(101L).city("Mumbai").state("MH").country("India").pinCode("441001").build();
        List<AddressDTO> addressDTOList = Arrays.asList(addressDTO, addressDTO1);

        Mockito.when(addressService.getEmployeeAddress(employeeId)).thenReturn(addressDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/employee/{employeeId}/addresses", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(101)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddressResponse_whenEmployeeNotFound() throws Exception {
        Long employeeId = 125L;
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();
        AddressDTO addressDTO1 = AddressDTO.builder().id(101L).city("Mumbai").state("MH").country("India").pinCode("441001").build();
        List<AddressDTO> addressDTOList = Arrays.asList(addressDTO, addressDTO1);

        Mockito.when(addressService.getEmployeeAddress(employeeId)).thenThrow(new EmployeeNotFoundException("Employee not found"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/employee/{employeeId}/addresses", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddressResponse_Failure() throws Exception {
        Long employeeId = 123L;
        AddressDTO addressDTO = AddressDTO.builder().id(100L).city("Pune").state("MH").country("India").pinCode("441809").build();
        AddressDTO addressDTO1 = AddressDTO.builder().id(101L).city("Mumbai").state("MH").country("India").pinCode("441001").build();
        List<AddressDTO> addressDTOList = Arrays.asList(addressDTO, addressDTO1);

        Mockito.when(addressService.getEmployeeAddress(employeeId)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/employee/{employeeId}/addresses", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetDataByAddressId() throws Exception {
        Long addressId = 100L;
        AddressDTO addressDTO = AddressDTO.builder().id(addressId).city("Pune").state("MH").country("IND").pinCode("441212").build();
        Mockito.when(addressService.getAddressById(addressId)).thenReturn(addressDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/getbyaddressid/{address-id}", addressId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pinCode").value(addressDTO.getPinCode()))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetDataByAddressId_whenAddressNotFound() throws Exception {
        Long addressId = 101L;
        AddressDTO addressDTO = AddressDTO.builder().id(addressId).city("Pune").state("MH").country("IND").pinCode("441212").build();

        Mockito.when(addressService.getAddressById(addressId)).thenThrow(new AddressNotFoundException("Address not found for given id " + addressId));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/getbyaddressid/{address-id}", addressId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testGetDataByAddressId_Failure() throws Exception {
        Long addressId = 102L;
        AddressDTO addressDTO = AddressDTO.builder().id(addressId).city("Pune").state("MH").country("IND").pinCode("441212").build();

        Mockito.when(addressService.getAddressById(addressId)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/getbyaddressid/{address-id}", addressId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

        Assertions.assertEquals(500, mvcResult.getResponse().getStatus());

    }

    @Test
    public void testUpdateAddress() throws Exception{
        Long addressId = 100L;
        AddressDTO addressDTO = AddressDTO.builder().id(addressId).city("Pune").state("MH").country("IND").pinCode("441212").build();

        Mockito.when(addressService.updateData(addressId,addressDTO)).thenReturn(addressDTO);
        String content = objectWriter.writeValueAsString(addressDTO);
       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/upadate-address/{address-id}", addressId)
                .contentType(MediaType.APPLICATION_JSON).content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(addressDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city",is("Pune")))
                .andReturn();

       Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateAddress_whenAddressNotFound() throws Exception{
        Long addressId = 105L;
        AddressDTO addressDTO = AddressDTO.builder().id(addressId).city("Pune").state("MH").country("IND").pinCode("441212").build();
        String content = objectWriter.writeValueAsString(addressDTO);
        Mockito.when(addressService.updateData(addressId,addressDTO)).thenThrow(new AddressNotFoundException("Address not found"));
       MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/upadate-address/{address-id}", addressId).contentType(MediaType.APPLICATION_JSON)
                .content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

       Assertions.assertEquals(404, mvcResult.getResponse().getStatus());

    }
    @Test
    public void testUpdateAddress_Failure() throws Exception{
        Long addressId = 105L;
        AddressDTO addressDTO = AddressDTO.builder().id(addressId).city("Pune").state("MH").country("IND").pinCode("441212").build();
        String content = objectWriter.writeValueAsString(addressDTO);

        Mockito.when(addressService.updateData(addressId, addressDTO)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/upadate-address/{address-id}", addressId).contentType(MediaType.APPLICATION_JSON)
                .content(content).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()).andReturn();

       Assertions.assertEquals(500, mvcResult.getResponse().getStatus());
    }

}
