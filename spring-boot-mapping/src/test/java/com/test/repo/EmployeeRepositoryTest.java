package com.test.repo;

import com.test.model.Address;
import com.test.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EmployeeRepositoryTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindDataByName_Success() {
        // Sample test data
        String existingName = "Pramij";
        Employee employee = Employee.builder().empId(123L).name(existingName).age(29).build();
        // Mock the behavior of the JpaRepository findDataByName method
        Mockito.when(employeeRepository.findDataByName(existingName)).thenReturn(Optional.of(employee));

        // Call the repository method
        Optional<Employee> result = employeeRepository.findDataByName(existingName);

        // Verify the result
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(existingName, result.get().getName());

        // Verify that the JpaRepository findDataByName method was called with the correct parameter
        Mockito.verify(employeeRepository).findDataByName(existingName);
    }

    @Test
    public void testFindDataByName_NonExistingName() {
        // Sample test data
        String nonExistingName = "Jane Smith";

        // Mock the behavior of the JpaRepository findDataByName method for a non-existing name
        Mockito.when(employeeRepository.findDataByName(nonExistingName)).thenReturn(Optional.empty());

        // Call the repository method
        Optional<Employee> result = employeeRepository.findDataByName(nonExistingName);

        // Verify the result
        Assertions.assertFalse(result.isPresent());

        // Verify that the JpaRepository findDataByName method was called with the correct parameter
        Mockito.verify(employeeRepository).findDataByName(nonExistingName);
    }

    @Test
    public void testFindEmployeesByCity_success() {
        String existingCity = "Pune";
        Employee employee = Employee.builder().empId(123L).name("Pramij").age(29).addresses(Stream.of(new Address(2L, existingCity, "123123", "Maha", "Ind", null), new Address(3L, "Bengal", "441212", "12321", "Ind", null)).toList()).build();
        List<Employee> employees = Collections.singletonList(employee);
        Mockito.when(employeeRepository.findEmployeesByCity(existingCity)).thenReturn(employees);
        List<Employee> employeeList = employeeRepository.findEmployeesByCity(existingCity);
        Assertions.assertFalse(employees.isEmpty());
        Assertions.assertEquals(1, employeeList.size());
        Mockito.verify(employeeRepository).findEmployeesByCity(existingCity);
    }

    @Test
    public void testFindEmployeesByCity_NonExistingCity(){
        String nonExistingCity = "Delhi";
        Mockito.when(employeeRepository.findEmployeesByCity(nonExistingCity)).thenReturn(Collections.EMPTY_LIST);
        List<Employee> employees = employeeRepository.findEmployeesByCity(nonExistingCity);
        Assertions.assertTrue(employees.isEmpty());
        Mockito.verify(employeeRepository).findEmployeesByCity(nonExistingCity);
    }

}
