package com.test.controller;

import com.test.contants.Constants;
import com.test.dto.AddressDTO;
import com.test.exception.AddressNotFoundException;
import com.test.exception.EmployeeNotFoundException;
import com.test.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/employee/{empId}/save")
    public ResponseEntity<AddressDTO> addNewAddress(@PathVariable(value = "empId") Long empId, @RequestBody AddressDTO addressDTO) throws EmployeeNotFoundException {
        AddressDTO addressDTO1 = null;
        try {
            addressDTO1 = addressService.add(empId, addressDTO);
            return new ResponseEntity<>(addressDTO1, HttpStatus.CREATED);
        } catch (EmployeeNotFoundException e) {
            throw new EmployeeNotFoundException(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getAddress")
<<<<<<< HEAD
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
=======
    public ResponseEntity<List<AddressDTO>> getAllAddresses() throws AddressNotFoundException {
        List<AddressDTO> addressDTOS = null;
>>>>>>> dev4branch
        try {
            List<AddressDTO> addressDTOS = addressService.addressDtoList();
            return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
<<<<<<< HEAD
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search Address data", e);
=======
        } catch (AddressNotFoundException e) {
            throw new AddressNotFoundException("No addresses found.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.MESSAGE, e);
>>>>>>> dev4branch
        }
    }

    @GetMapping("/employee/{employeeId}/addresses")
    public ResponseEntity<List<AddressDTO>> addressResponse(@PathVariable(value = "employeeId") Long empId) {
        try {
            List<AddressDTO> addressDTOS = addressService.getEmployeeAddress(empId);
            return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.MESSAGE, e);
        }
    }

    @GetMapping("/getbyaddressid/{address-id}")
    public ResponseEntity<AddressDTO> getDataByAddressId(@PathVariable("address-id") Long id) throws AddressNotFoundException {
        AddressDTO addressDTO = null;
        try {
            addressDTO = addressService.getAddressById(id);
            return new ResponseEntity<>(addressDTO, HttpStatus.OK);
        } catch (AddressNotFoundException e) {
            throw new AddressNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.MESSAGE, e);
        }
    }

    @PutMapping("/upadate-address/{address-id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable(value = "address-id") Long id, @RequestBody AddressDTO addressDTO) throws AddressNotFoundException {
        try {
            AddressDTO addressDTO1 = addressService.updateData(id, addressDTO);
            return new ResponseEntity<>(addressDTO1, HttpStatus.OK);
        } catch (AddressNotFoundException e) {
            throw new AddressNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.MESSAGE, e);
        }
    }
}