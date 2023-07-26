package com.test.repo;

import com.test.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

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

}
