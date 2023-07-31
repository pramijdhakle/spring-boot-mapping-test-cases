package com.test.controller;

import com.test.contants.Constants;
import com.test.dto.CustomeResponse;
import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Employee;
import com.test.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public EmployeeController(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/save")
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO employee1 = employeeService.saveEmployee(employeeDTO);
            return new ResponseEntity<>(employee1, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save employee", e);
        }
    }

    @GetMapping("/getAllEmployee")
    public ResponseEntity<List<EmployeeDTO>> getAllemployees() {
        try {
            List<EmployeeDTO> employeeDTOS = employeeService.getAllEmployee();
            return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve employees", e);
        }
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateData(@PathVariable(value = "employeeId") Long empId, @RequestBody EmployeeDTO employeeDTO) throws EmployeeNotFoundException {
        try {
            EmployeeDTO employeeDTO1 = employeeService.updateData(empId, employeeDTO);
            return new ResponseEntity<>(employeeDTO1, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            // Handle the exception or rethrow it
            throw new EmployeeNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update employee data", e);
        }
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteData(@PathVariable(value = "employeeId") Long empId) throws EmployeeInactiveException, EmployeeNotFoundException {
        try {
            employeeService.deleteEmployeeData(empId);
            return new ResponseEntity<>("Data Deleted Successfully !!!", HttpStatus.OK);
        } catch (EmployeeInactiveException e) {
            // Handle the exception or rethrow it
            throw new EmployeeInactiveException(e.getMessage());
        } catch (EmployeeNotFoundException e) {
            // Handle the exception or rethrow it
            throw new EmployeeNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete employee data", e);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchByAnyInput(@RequestBody EmployeeDTO employee) {
        try {
            List<EmployeeDTO> dataByAnyInput = employeeService.getDataByAnyInput(employee);
            return new ResponseEntity<>(dataByAnyInput, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search employee data", e);
        }
    }

    @GetMapping("/getbyid/{employeeId}")
    public ResponseEntity<EmployeeDTO> getDataByEmployeeId(@PathVariable(value = "employeeId") Long empId) throws EmployeeNotFoundException {
        try {
            EmployeeDTO employeeDTO = employeeService.getDataById(empId);
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            // Handle the exception or rethrow it
            throw new EmployeeNotFoundException("Employee Not Found");
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Get employee data", e);
        }
    }

    @GetMapping("/getbyname/{employee-name}")
    public ResponseEntity<EmployeeDTO> getDataByName(@PathVariable(value = "employee-name") String name) throws EmployeeNotFoundException {
        try {
            EmployeeDTO employeeDTO = employeeService.getDataByName(name);
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new EmployeeNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Get employee data", e);
        }

    }

    @PostMapping("/searchbyquery")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySearchCriteria(@RequestBody Employee employeeDTO) throws EmployeeNotFoundException {
        try {
            List<EmployeeDTO> employeeDTOS = employeeService.findDataBySearchEmployee(employeeDTO);
            return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new EmployeeNotFoundException(e.getMessage());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.ERROR);
        }
    }

    @GetMapping("/searchbyquery2")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeData(@RequestParam(value = "employeeId", required = false) Long empId, @RequestParam(value = "pinCode", required = false) String pinCode) throws EmployeeNotFoundException {
        try {
            List<EmployeeDTO> employeeDTOS = employeeService.getDataByQuery(empId, pinCode);
            return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new EmployeeNotFoundException(e.getMessage());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch the data!!", e);
        }
    }

    @GetMapping("/getByCity/{city}")
    public ResponseEntity<List<EmployeeDTO>> getByCity(@PathVariable String city) {
        try {
            List<EmployeeDTO> employees = employeeService.findEmployeesByCity(city);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data Not found", e);
        }

    }
}
