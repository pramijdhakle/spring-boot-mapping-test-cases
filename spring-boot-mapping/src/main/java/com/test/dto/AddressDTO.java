package com.test.dto;

import com.test.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressDTO {
    private Long id;
    private String city;
    private String pinCode;
    private String state;
    private String country;
}
