package com.test.serviceimpl;

import com.test.dto.AddressDTO;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;
import com.test.model.Employee;
import com.test.repo.AddressRepository;
import com.test.repo.EmployeeRepository;
import com.test.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
    }

    /**
     * @param empId
     * @param addressDTO
     * @throws EmployeeNotFoundException if the employee with the given empId is not found
     * @return
     */
    @Override
    public AddressDTO add(Long empId, AddressDTO addressDTO) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
        }
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setEmployee(employee.get());
        Address saveAddress = addressRepository.save(address);

        return modelMapper.map(saveAddress, AddressDTO.class);
    }

    /**
     * @return
     */
    @Override
    public List<AddressDTO> addressDtoList() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOS = addresses.stream().map(mapToDTO -> modelMapper.map(mapToDTO, AddressDTO.class)).collect(Collectors.toList());
        return addressDTOS;
    }

    /**
     * @param empId
     * @return
     */
    @Override
    public List<AddressDTO> addressDtos(Long empId) throws EmployeeNotFoundException {
        try {
            Optional<Employee> employee = employeeRepository.findById(empId);
            if (employee.isEmpty()) {
                throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
            }

            List<Address> byEmpId = addressRepository.findByEmployee(employee.get());
            List<AddressDTO> addressDTOList = byEmpId.stream().map(mapToDto -> modelMapper.map(mapToDto, AddressDTO.class)).collect(Collectors.toList());
            return addressDTOList;
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new RuntimeException(e);
        }
    }
}
