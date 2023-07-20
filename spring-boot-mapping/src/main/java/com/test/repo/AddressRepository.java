package com.test.repo;

import com.test.model.Address;
import com.test.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {


    List<Address> findByEmployee(Employee employee);
}
