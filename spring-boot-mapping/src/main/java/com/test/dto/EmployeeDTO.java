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

    public EmployeeDTO(Long empId, String name, Integer age, Boolean active, Long phoneNumber, String designation, Double salary) {
        this.empId = empId;
        this.name = name;
        this.age = age;
        this.active = active;
        this.phoneNumber = phoneNumber;
        this.designation = designation;
        this.salary = salary;
    }
}
