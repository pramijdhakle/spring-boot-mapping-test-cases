package com.test.serviceimpl;

import com.test.dto.AddressDTO;
import com.test.exception.AddressNotFoundException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;
import com.test.model.Employee;
import com.test.repo.AddressRepository;
import com.test.repo.EmployeeRepository;
import com.test.service.AddressService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        if (employee.isPresent()) {
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
<<<<<<< HEAD
    public List<AddressDTO> addressDtoList() {
        try {
            List<Address> addresses = addressRepository.findAll();
            if (addresses.isEmpty()) {
                throw new AddressNotFoundException("No addresses found.");
            }
            return addresses.stream().map(mapToDTO -> modelMapper.map(mapToDTO, AddressDTO.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
=======
    public List<AddressDTO> addressDtoList() throws AddressNotFoundException {
        List<Address> addresses = addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new AddressNotFoundException("No addresses found.");
>>>>>>> dev4branch
        }
        return Stream.of(addresses).flatMap(List::stream).map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    /**
     * @param empId
     * @return
     */
    @Override
<<<<<<< HEAD
    public List<AddressDTO> addressDtos(Long empId) {
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
=======
    public List<AddressDTO> getEmployeeAddress(Long empId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isPresent()) {
            List<Address> addresses = addressRepository.findByEmployee(employee.get());
            return Stream.of(addresses)
                    .flatMap(List::stream)
                    .map(mapToDto -> modelMapper.map(mapToDto, AddressDTO.class))
                    .toList();
        } else {
            throw new EmployeeNotFoundException("Employee not found with empId: " + empId);
>>>>>>> dev4branch
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

<<<<<<< HEAD
    @Override
    public void updateAddress(Address address) {
        Address address1 = new Address();
        addressRepository.save(address);
=======
    /**
     * @param id
     * @param addressDTO
     * @return
     */
    @Override
    @Transactional
    public AddressDTO updateData(Long id, AddressDTO addressDTO) throws AddressNotFoundException {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()){
            Address address = optionalAddress.get();
            Address address1 = modelMapper.map(addressDTO, Address.class);
            // Update only the non-null properties from addressDTO
            if (addressDTO.getCity() != null) {
                address.setCity(addressDTO.getCity());
            }
            if (addressDTO.getState() != null) {
                address.setState(addressDTO.getState());
            }
            if (addressDTO.getCountry() != null) {
                address.setCountry(addressDTO.getCountry());
            }
            if (addressDTO.getPinCode() != null) {
                address.setPinCode(addressDTO.getPinCode());
            }

            Address save = addressRepository.save(address);
            AddressDTO updatedAddress = modelMapper.map(save, AddressDTO.class);
            return updatedAddress;
        }else {
            throw new AddressNotFoundException("Address is not available for the id : "+ id);
        }
>>>>>>> dev4branch
    }
}
