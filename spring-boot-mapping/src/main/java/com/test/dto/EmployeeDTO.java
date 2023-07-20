package com.test.dto;

import com.test.model.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    private Long empId;
    private String name;
    private Integer age;
    private Boolean active;
    private Long phoneNumber = (long) (Math.random() * Math.pow(10, 10));
    private String designation;
    private Double salary = Math.random() * 100000;
    private List<Address> addresses;

}
