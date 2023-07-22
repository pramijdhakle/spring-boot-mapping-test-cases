package com.test.controller;

import com.test.dto.AddressDTO;
import com.test.exception.AddressMappingException;
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
            throw new RuntimeException(e);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getAddress")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        try {
            List<AddressDTO> addressDTOS = addressService.addressDtoList();
            return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search Address data", e);
        }
    }

    @GetMapping("/employee/{employeeId}/addresses")
    public ResponseEntity<List<AddressDTO>> addressResponse(@PathVariable(value = "employeeId") Long empId) throws EmployeeNotFoundException {
        List<AddressDTO> addressDTOS = null;
        try {
            addressDTOS = addressService.addressDtos(empId);
            return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to search Address data", e);
        }

    }

    @GetMapping("/getbyaddressid/{address-id}")
    public ResponseEntity<AddressDTO> getDataByAddressId(@PathVariable("address-id") Long id) throws AddressNotFoundException {
        AddressDTO addressDTO = null;
        try {
             addressDTO = addressService.getAddressById(id);
            return new ResponseEntity<>(addressDTO, HttpStatus.OK);
        } catch (AddressNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search Address data", e);
        }
    }
}