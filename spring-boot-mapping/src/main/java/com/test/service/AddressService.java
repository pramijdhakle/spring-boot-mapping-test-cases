package com.test.service;

import com.test.dto.AddressDTO;
import com.test.exception.AddressNotFoundException;
import com.test.exception.EmployeeNotFoundException;

import java.util.List;

public interface AddressService {

    AddressDTO add(Long empId, AddressDTO addressDTO) throws EmployeeNotFoundException;

    List<AddressDTO> addressDtoList() throws AddressNotFoundException;

    List<AddressDTO> getEmployeeAddress(Long empId) throws EmployeeNotFoundException;

    AddressDTO getAddressById(Long id) throws AddressNotFoundException;

    AddressDTO updateData(Long id, AddressDTO addressDTO) throws AddressNotFoundException;

}
