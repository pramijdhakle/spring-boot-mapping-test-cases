package com.test.service;

import com.test.dto.AddressDTO;
import com.test.exception.AddressNotFoundException;
import com.test.exception.EmployeeNotFoundException;
import com.test.model.Address;

import java.util.List;

public interface AddressService {

    AddressDTO add(Long empId, AddressDTO addressDTO) throws EmployeeNotFoundException;
<<<<<<< HEAD

    List<AddressDTO> addressDtoList();
=======
>>>>>>> dev4branch

    List<AddressDTO> addressDtoList() throws AddressNotFoundException;

    List<AddressDTO> getEmployeeAddress(Long empId) throws EmployeeNotFoundException;

    AddressDTO getAddressById(Long id) throws AddressNotFoundException;

<<<<<<< HEAD
    void updateAddress(Address address);
=======
    AddressDTO updateData(Long id, AddressDTO addressDTO) throws AddressNotFoundException;

>>>>>>> dev4branch
}
