package com.test.serviceimpl;

import com.test.dto.AddressDTO;
import com.test.exception.AddressNotFoundException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;
import com.test.model.Employee;
import com.test.repo.AddressRepository;
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
import java.util.List;
import java.util.Optional;

public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SneakyThrows
    @Test
    public void testAdd() {
        Employee employee = new Employee();
        Long empId = 1L;
        Address address = new Address();
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        Mockito.when(modelMapper.map(addressDTO, Address.class)).thenReturn(address);
        Mockito.when(addressRepository.save(address)).thenReturn(address);
        Mockito.when(modelMapper.map(address, AddressDTO.class)).thenReturn(addressDTO);

        AddressDTO result = addressService.add(empId, addressDTO);
        Assertions.assertEquals(addressDTO, result);
        Mockito.verify(employeeRepository).findById(empId);

    }

    @Test
    public void testAdd_WhenEmployeeNotFound() {
        Long empId = 1L;
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(employeeRepository.findById(empId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> addressService.add(empId, addressDTO));

        Mockito.verify(employeeRepository).findById(empId);
        Mockito.verify(addressRepository, Mockito.never()).save(Mockito.any());
    }

    @SneakyThrows
    @Test
    public void testAddressDtoList() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1L, "Pune", "441424", "Mah", "Ind", null));
        Mockito.when(addressRepository.findAll()).thenReturn(addresses);
        Mockito.when(modelMapper.map(Mockito.any(Address.class),
                        Mockito.eq(AddressDTO.class)))
                .thenReturn(new AddressDTO());

        List<AddressDTO> result = addressService.addressDtoList();
        Assertions.assertEquals(addresses.size(), result.size());
        Mockito.verify(addressRepository).findAll();
    }

    @Test
    public void testAddressDtoList_WhenAddressNotFoundException() {
        List<Address> addresses = new ArrayList<>();
        Mockito.when(addressRepository.findAll())
                .thenReturn(addresses);

        Assertions.assertThrows(AddressNotFoundException.class,
                () -> addressService.addressDtoList());

        Mockito.verify(addressRepository).findAll();
    }

    @SneakyThrows
    @Test
    public void testGetEmployeeAddress() {
        Long empId = 1L;
        Employee employee = new Employee();
        List<Address> addresses = new ArrayList<>();
        Mockito.when(employeeRepository.findById(empId))
                .thenReturn(Optional.of(employee));

        Mockito.when(addressRepository.findByEmployee(employee))
                .thenReturn(addresses);

        Mockito.when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDTO.class)))
                .thenReturn(new AddressDTO());

        List<AddressDTO> result = addressService.getEmployeeAddress(empId);
        Assertions.assertEquals(addresses.size(), result.size());
        Mockito.verify(addressRepository).findByEmployee(employee);
    }

    @Test
    public void testGetEmployeeAddress_WhenEmployeeNotFound() {
        Long empId = 1L;
        Mockito.when(employeeRepository.findById(empId)).
                thenReturn(Optional.empty());

        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> addressService.getEmployeeAddress(empId));

        Mockito.verify(employeeRepository).findById(empId);
    }

    @SneakyThrows
    @Test
    public void testGetAddressById(){
        Long addressId = 1L;
        Address address = new Address();
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        Mockito.when(modelMapper.map(address, AddressDTO.class)).thenReturn(addressDTO);
        AddressDTO result = addressService.getAddressById(addressId);
        Assertions.assertEquals(addressDTO, result);
        Mockito.verify(addressRepository).findById(addressId);
    }

    @Test
    public void testGetAddressById_WhenAddressNotFound(){
        Long addressId = 1L;
        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.empty());
        Assertions.assertThrows(AddressNotFoundException.class, ()-> addressService.getAddressById(addressId));
        Mockito.verify(addressRepository).findById(addressId);
    }

    @SneakyThrows
    @Test
    public void testUpdateData(){
        Long addressId = 1L;
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("New City");
        addressDTO.setState("New State");
        addressDTO.setCountry("New Country");
        addressDTO.setPinCode("New PinCode");

        Address existingAddress = new Address();
        existingAddress.setId(addressId);
        existingAddress.setCity("Old City");
        existingAddress.setState("Old State");
        existingAddress.setCountry("Old Country");
        existingAddress.setPinCode("Old PinCode");
        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        Mockito.when(addressRepository.save(existingAddress)).thenReturn(existingAddress);
        Mockito.when(modelMapper.map(existingAddress, AddressDTO.class)).thenReturn(addressDTO);

        AddressDTO updatedAddress = addressService.updateData(addressId, addressDTO);
        Assertions.assertEquals(addressDTO, updatedAddress);
        Assertions.assertEquals(addressDTO.getCity(), updatedAddress.getCity());
        Assertions.assertEquals(addressDTO.getState(), updatedAddress.getState());
        Assertions.assertEquals(addressDTO.getCountry(), updatedAddress.getCountry());
        Assertions.assertEquals(addressDTO.getPinCode(), updatedAddress.getPinCode());

        Mockito.verify(addressRepository).findById(addressId);
        Mockito.verify(addressRepository, Mockito.atLeastOnce()).save(Mockito.any());

    }

    @Test
    public void testUpdateData_WhenAddressNotFound(){
        Long addressId = 1L;
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.empty());
        Assertions.assertThrows(AddressNotFoundException.class, ()-> addressService.updateData(addressId,addressDTO));
        Mockito.verify(addressRepository).findById(addressId);
        Mockito.verify(addressRepository, Mockito.never()).save(Mockito.any());
    }
}
