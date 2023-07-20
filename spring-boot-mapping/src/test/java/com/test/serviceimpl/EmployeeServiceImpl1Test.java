package com.test.serviceimpl;


import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;
import com.test.model.Employee;
import com.test.repo.EmployeeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImpl1Test {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();

        Mockito.when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Mockito.when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        assertEquals(employeeDTO, employeeService.saveEmployee(employeeDTO));
        Mockito.verify(employeeRepository).save(employee);

    }

    @Test
    public void testGetAllEmployee() {
        // Prepare
        List<Employee> employees = new ArrayList<>();
        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        Mockito.when(modelMapper.map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class))).thenReturn(new EmployeeDTO());
        // Act
        List<EmployeeDTO> result = employeeService.getAllEmployee();
        // Assert
        Assertions.assertEquals(employees.size(), result.size());
        Mockito.verify(employeeRepository).findAll();
    }

    @Test
    public void testDeleteEmployeeData() {
        Long empId = 1L;
        Employee employee = new Employee();
        employee.setActive(true);
        Optional<Employee> optionalEmployee = Optional.of(employee);
        Mockito.when(employeeRepository.findById(empId)).thenReturn(optionalEmployee);

        Assertions.assertDoesNotThrow(() -> employeeService.deleteEmployeeData(empId));
        Mockito.verify(employeeRepository).findById(empId);
        Mockito.verify(employeeRepository).save(employee);


    }

    @Test
    public void testDeleteEmployeeData_WhenEmployeeNotFound() {
        Long empId = 1L;
        Optional<Employee> employee = Optional.empty();
        Mockito.when(employeeRepository.findById(empId)).thenReturn(employee);
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployeeData(empId));

        Mockito.verify(employeeRepository).findById(empId);
        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testDeleteEmployeeData_WhenEmployeeInactive() {
        Long empId = 1L;
        Employee employee = new Employee();
        employee.setActive(false);
        Optional<Employee> optionalEmployee = Optional.of(employee);
        Mockito.when(employeeRepository.findById(empId))
                .thenReturn(optionalEmployee);

        Assertions.assertThrows(EmployeeInactiveException.class,
                () -> employeeService.deleteEmployeeData(empId));

        Mockito.verify(employeeRepository).findById(empId);
        Mockito.verify(employeeRepository, Mockito.never())
                .save(Mockito.any());

    }

    @SneakyThrows
    @Test
    public void testUpdateEmployeeData() {
        Long empId = 1L;
        Employee employee = new Employee();
        EmployeeDTO employeeDTO = new EmployeeDTO();

        Mockito.when(employeeRepository.findById(empId))
                .thenReturn(Optional.of(employee));
        Mockito.when(modelMapper.map(employeeDTO, Employee.class))
                .thenReturn(employee);
        Mockito.when(employeeRepository.save(employee))
                .thenReturn(employee);
        Mockito.when(modelMapper.map(employee, EmployeeDTO.class))
                .thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.updateData(empId, employeeDTO);
        Assertions.assertEquals(employeeDTO, result);
        Mockito.verify(employeeRepository).findById(empId);
        Mockito.verify(employeeRepository).save(employee);
    }

    @Test
    public void testUpdateEmployeeData_WhenEmployeeNotFound() {
        Long empId = 1L;
        Optional<Employee> optionalEmployee = Optional.empty();
        Mockito.when(employeeRepository.findById(empId))
                .thenReturn(optionalEmployee);
        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.updateData(empId, new EmployeeDTO()));

        Mockito.verify(employeeRepository).findById(empId);
        Mockito.verify(employeeRepository, Mockito.never())
                .save(Mockito.any());
    }

    @SneakyThrows
    @Test
    public void testGetDataById()  {

        Long empId = 1L;
        Employee employee = new Employee();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Mockito.when(employeeRepository.findById(empId))
                .thenReturn(Optional.of(employee));

        Mockito.when(modelMapper.map(employee, EmployeeDTO.class))
                .thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.getDataById(empId);
        Assertions.assertEquals(employeeDTO,result);
        Assertions.assertEquals(employeeDTO.getEmpId(), result.getEmpId());

    }
    @Test
    public void testGetDataById_employeeNotFound() {
        // Mocking the behavior of the employeeRepository.findById method
        Long empId = 1L;
        Mockito.when(employeeRepository.findById(empId)).thenReturn(Optional.empty());

        // Calling the method under test and expecting an exception to be thrown
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.getDataById(empId));

        // Verifying that the employeeRepository.findById was called with the correct empId
        Mockito.verify(employeeRepository).findById(empId);
    }

/*
    @Test
    public void testGetDataByAnyInput(){
        List<Employee> employees = new ArrayList<>();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employees.add(new Employee(
                1L,"Pramij",34,true,123-456-7890L,"Eng",727727.6,
                Stream.of(new Address(
                        2L,"Mum","7888","Maha","Ind",null
                )).toList()
        ));

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        Mockito.when(modelMapper.map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO
                .class))).thenReturn(new EmployeeDTO());
        List<EmployeeDTO> employeeDTOList  = employeeService.getDataByAnyInput(employeeDTO);
        Assertions.assertEquals(employeeDTOList.size(), employees.size());
    }
*/

   /* @Test
    void getDataByAnyInput_shouldReturnEmployeeDTOList() {
        Employee emp1 = new Employee(1L, "John", 30, true, 788766L,"Manager", 50000.0);
        Employee emp2 = new Employee(2L, "Alice", 25, false,6788766L, "Developer", 60000.0);
        List<Employee> employees = Arrays.asList(emp1, emp2);
        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        // Mocking the modelMapper's map method
        EmployeeDTO empDTO1 = new EmployeeDTO(1L, "John", 30, true, 1234567890L, "Manager", 50000.0);
        EmployeeDTO empDTO2 = new EmployeeDTO(2L, "Alice", 25, false, 9876543210L, "Developer", 60000.0);
        Mockito.when(modelMapper.map(Mockito.eq(emp1), Mockito.eq(EmployeeDTO.class))).thenReturn(empDTO1);
        Mockito.when(modelMapper.map(Mockito.eq(emp2), Mockito.eq(EmployeeDTO.class))).thenReturn(empDTO2);

        // Creating an EmployeeDTO with desired search criteria
        EmployeeDTO searchCriteria = new EmployeeDTO();
        searchCriteria.setName("John");

        // Calling the method under test
        List<EmployeeDTO> result = employeeService.getDataByAnyInput(searchCriteria);

        // Asserting the result
        assertEquals(1, result.size());
        EmployeeDTO foundEmployee = result.get(0);
        assertEquals(empDTO1.getEmpId(), foundEmployee.getEmpId());
        assertEquals(empDTO1.getName(), foundEmployee.getName());
        assertEquals(empDTO1.getActive(), foundEmployee.getActive());
        // ... add more assertions for other fields as needed

        // Verifying that employeeRepository's findAll method was called once
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();

        // Verifying that modelMapper's map method was called for each employee
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.eq(emp1),
                Mockito.eq(EmployeeDTO.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.eq(emp2),
                Mockito.eq(EmployeeDTO.class));
    }*/
}



