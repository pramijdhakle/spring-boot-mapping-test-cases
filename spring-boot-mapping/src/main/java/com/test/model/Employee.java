package com.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @SequenceGenerator(name = "seq_id", sequenceName = "seq_id", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id")
    private Long empId;
    private String name;
    private Integer age;

    @JsonProperty(namespace = "status")
    private Boolean active ;
    @Column(name = "PHONE_NUMBER")
    private Long phoneNumber = (long) (Math.random() * Math.pow(10, 10));
    private String designation;
    private Double salary = Math.random() * 100000;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Address> addresses;

    public Employee(Long empId, String name, Integer age, Boolean active, Long phoneNumber, String designation, Double salary) {
        this.empId = empId;
        this.name = name;
        this.age = age;
        this.active = active;
        this.phoneNumber = phoneNumber;
        this.designation = designation;
        this.salary = salary;
    }
}
