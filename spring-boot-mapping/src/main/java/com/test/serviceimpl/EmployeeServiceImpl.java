package com.test.serviceimpl;

import com.test.dto.EmployeeDTO;
import com.test.exception.EmployeeInactiveException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;
import com.test.model.Employee;
import com.test.repo.EmployeeRepository;
import com.test.service.AddressService;
import com.test.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    private final AddressService addressService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, AddressService addressService) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.addressService = addressService;
    }
    /**
     * @param employee
     * @return
     */

    /**
     * @param employeeDTO
     * @return
     */
    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setActive(true);
        Employee save = employeeRepository.save(employee);
        return modelMapper.map(save, EmployeeDTO.class);
    }

    /**
     * @return
     */
    @Override
    public List<EmployeeDTO> getAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employees.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());
        return employeeDTOList;
    }

    /**
     * @param empId
     */
    @Override
    public void deleteEmployeeData(Long empId) throws EmployeeNotFoundException, EmployeeInactiveException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            if (existingEmployee.getActive()) {
                existingEmployee.setActive(false);
                employeeRepository.save(existingEmployee);
            } else {
                throw new EmployeeInactiveException("Employee is already inactive.");
            }
        } else {
            throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
        }
    }

    /**
     * @param empId
     * @param employeeDTO
     * @return
     */
    @Override
    public EmployeeDTO updateData(Long empId, EmployeeDTO employeeDTO) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            Employee map = modelMapper.map(employeeDTO, Employee.class);
            existingEmployee.setName(map.getName());
            existingEmployee.setAge(map.getAge());
            existingEmployee.setDesignation(map.getDesignation());
            existingEmployee.setSalary(map.getSalary());
            existingEmployee.setPhoneNumber(map.getPhoneNumber());
            existingEmployee.setActive(existingEmployee.getActive());
            // existingEmployee.setAddresses(map.getAddresses());
            for (Address address : map.getAddresses()) {
                address.setEmployee(existingEmployee);
                addressService.updateAddress(address);
            }
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return modelMapper.map(updatedEmployee, EmployeeDTO.class);
        } else {
            throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
        }
    }

    @Override
    public List<EmployeeDTO> getDataByAnyInput(EmployeeDTO employee) {
        List<Employee> employees = employeeRepository.findAll();

        List<EmployeeDTO> employeeDTO1 = employees.stream().map(mapToDto -> modelMapper.map(mapToDto, EmployeeDTO.class)).filter(employeeDTO -> employeeDTO.getEmpId() == employee.getEmpId() || employeeDTO.getName().equals(employee.getName()) || employeeDTO.getActive().equals(employee.getActive()) || employeeDTO.getAge() == employee.getAge() || employeeDTO.getDesignation().equals(employee.getDesignation()) || employeeDTO.getPhoneNumber() == employee.getPhoneNumber() || employeeDTO.getSalary() == employee.getSalary() || employeeDTO.getAddresses().equals(employee.getAddresses())).collect(Collectors.toList());

        return employeeDTO1;
    }

    @Override
    public EmployeeDTO getDataById(Long empId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isPresent()) {
            Employee employee2 = employee.get();
            return modelMapper.map(employee2, EmployeeDTO.class);

        } else {
            throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
        }


    }

    @Override
    public EmployeeDTO getDataByName(String name) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findDataByName(name);
        if (employee.isPresent()) {
            Employee employee1 = employee.get();
            return modelMapper.map(employee1, EmployeeDTO.class);
        } else {
            throw new EmployeeNotFoundException("Employee not found with Name: " + name);
        }

    }

    /**
     * @param
     * @return
     */
    @Override
    public List<EmployeeDTO> findDataBySearchEmployee(Employee employee) throws EmployeeNotFoundException {
        List<Employee> employeeList = employeeRepository.findDataBySearchEmployee(employee.getName(), employee.getAge(), employee.getDesignation(), employee.getActive());
        if (employeeList.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found for the given input !!");
        } else {
            List<EmployeeDTO> employeeDTOList =
                    Stream.of(employeeList).flatMap(List::stream).map(entityToDto -> modelMapper.map(entityToDto, EmployeeDTO.class)).toList();
            return employeeDTOList;
        }
    }

    /**
     * @param
     * @return
     */
    @Override
    public List<EmployeeDTO> getDataByQuery(Long empId, String pinCode) {
        List<Employee> employeeDTOS = employeeRepository.findDataByQuery(empId, pinCode);
        return employeeDTOS.stream().map(x-> modelMapper.map(x, EmployeeDTO.class)).collect(Collectors.toList());
    }

}
/**
 * @param employee
 * @return Get Data By Any Input and it returns Single Object
 */
   /* @Override
    public EmployeeDTO getDataByAnyInput(EmployeeDTO employee) {
        List<Employee> employees = employeeRepository.findAll();
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        List<EmployeeDTO> employeeDTOS = employees.stream().map(mapToDto -> modelMapper.map(mapToDto, EmployeeDTO.class)).collect(Collectors.toList());
        for (EmployeeDTO employeeDTO : employeeDTOS) {
            if (employeeDTO.getEmpId() == employee.getEmpId() || employeeDTO.getName().equals(employee.getName()) || employeeDTO.getActive().equals(employee.getActive()) || employeeDTO.getAge() == employee.getAge() || employeeDTO.getDesignation().equals(employee.getDesignation()) || employeeDTO.getPhoneNumber() == employee.getPhoneNumber() || employeeDTO.getSalary() == employee.getSalary() || employeeDTO.getAddresses().equals(employee.getAddresses())) {
                employeeDTO1.setEmpId(employeeDTO.getEmpId());
                employeeDTO1.setName(employeeDTO.getName());
                employeeDTO1.setActive(employeeDTO.getActive());
                employeeDTO1.setAge(employeeDTO.getAge());
                employeeDTO1.setDesignation(employeeDTO.getDesignation());
                employeeDTO1.setSalary(employeeDTO.getSalary());
                employeeDTO1.setPhoneNumber(employeeDTO.getPhoneNumber());
                employeeDTO1.setAddresses(employeeDTO.getAddresses());
            }
        }
        return employeeDTO1;
    }*/

//  Get Data By Any Input and it returns Single Object based on what we search.
// Using Java 8 Stream API
//    @Override
//    public EmployeeDTO getDataByAnyInput(EmployeeDTO employee) {
//        List<Employee> employees = employeeRepository.findAll();
//
//        List<EmployeeDTO> employeeDTO1 = employees.stream().map(mapToDto -> modelMapper.map(mapToDto, EmployeeDTO.class))
//                .filter(employeeDTO -> employeeDTO.getEmpId() == employee.getEmpId()
//                        || employeeDTO.getName().equals(employee.getName())
//                        || employeeDTO.getActive().equals(employee.getActive())
//                        || employeeDTO.getAge() == employee.getAge()
//                        || employeeDTO.getDesignation().equals(employee.getDesignation())
//                        || employeeDTO.getPhoneNumber() == employee.getPhoneNumber()
//                        || employeeDTO.getSalary() == employee.getSalary()
//                        || employeeDTO.getAddresses().equals(employee.getAddresses()))
//                .findFirst().orElse(new EmployeeDTO());
//
//        return employeeDTO1;
//    }

