package com.test.service;

import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getAllEmployee();

    void deleteEmployeeData(Long empId) throws EmployeeNotFoundException, EmployeeInactiveException;

    EmployeeDTO updateData(Long empId, EmployeeDTO employeeDTO) throws EmployeeNotFoundException;

    List<EmployeeDTO> getDataByAnyInput(EmployeeDTO employee);

    EmployeeDTO getDataById(Long empId) throws EmployeeNotFoundException;

    EmployeeDTO getDataByName(String name) throws EmployeeNotFoundException;
}
