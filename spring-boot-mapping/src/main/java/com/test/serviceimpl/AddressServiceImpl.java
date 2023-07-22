package com.test.serviceimpl;

import com.test.dto.AddressDTO;
import com.test.exception.AddressMappingException;
import com.test.exception.AddressNotFoundException;
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
     * @return
     * @throws EmployeeNotFoundException if the employee with the given empId is not found
     */
    @Override
    public AddressDTO add(Long empId, AddressDTO addressDTO) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isEmpty()) {
            Address address = modelMapper.map(addressDTO, Address.class);
            address.setEmployee(employee.get());
            Address saveAddress = addressRepository.save(address);

            return modelMapper.map(saveAddress, AddressDTO.class);
        } else {
            throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
        }

    }

    /**
     * @return
     */
    @Override
    public List<AddressDTO> addressDtoList() throws Exception {
        try {
            List<Address> addresses = addressRepository.findAll();
            if (addresses.isEmpty()) {
                throw new AddressNotFoundException("No addresses found.");
            }
            return addresses.stream().map(mapToDTO -> modelMapper.map(mapToDTO, AddressDTO.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AddressMappingException("Error while mapping Address entities to DTOs.");
        }
    }

    /**
     * @param empId
     * @return
     */
    @Override
    public List<AddressDTO> addressDtos(Long empId) throws EmployeeNotFoundException {
        try {
            Optional<Employee> employee = employeeRepository.findById(empId);
            if (employee.isPresent()) {
                List<Address> byEmpId = addressRepository.findByEmployee(employee.get());
                List<AddressDTO> addressDTOList = byEmpId.stream().map(mapToDto -> modelMapper.map(mapToDto, AddressDTO.class)).collect(Collectors.toList());
                return addressDTOList;
            } else {
                throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
            }
        } catch (Exception e) {
            // Handle the exception or rethrow it
            throw new RuntimeException(e);
        }
    }

    @Override
    public AddressDTO getAddressById(Long id) throws AddressNotFoundException {

        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            return modelMapper.map(address.get(), AddressDTO.class);
        } else {
            throw new AddressNotFoundException("Address not found with id : " + id);
        }

    }

    @Override
    public void updateAddress(Address address) {
        Address address1 = new Address();
        addressRepository.save(address);
    }
}
