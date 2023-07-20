package com.test.controller;

import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Employee;
import com.test.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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
    public ResponseEntity<List<EmployeeDTO>> employees() {
        try {
            List<EmployeeDTO> employeeDTOS = employeeService.getAllEmployee();
            return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve employees", e);
        }
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateData(@PathVariable(value = "employeeId") Long empId, @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO employeeDTO1 = employeeService.updateData(empId, employeeDTO);
            return new ResponseEntity<>(employeeDTO1, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            // Handle the exception or rethrow it
            throw new RuntimeException(e);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update employee data", e);
        }
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteData(@PathVariable(value = "employeeId") Long empId) {
        try {
            employeeService.deleteEmployeeData(empId);
            return new ResponseEntity<>("Data Deleted Successfully !!!", HttpStatus.OK);
        } catch (EmployeeInactiveException e) {
            // Handle the exception or rethrow it
            throw new RuntimeException(e);
        } catch (EmployeeNotFoundException e) {
            // Handle the exception or rethrow it
            throw new RuntimeException(e);
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
    public ResponseEntity<EmployeeDTO> getDataByEmployeeId(@PathVariable(value = "employeeId")
                                                               Long empId) throws EmployeeNotFoundException {
        try {
            EmployeeDTO employeeDTO = employeeService.getDataById(empId);
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        }catch (EmployeeNotFoundException e) {
            // Handle the exception or rethrow it
            throw new RuntimeException(e);
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete employee data", e);
        }

    }
}