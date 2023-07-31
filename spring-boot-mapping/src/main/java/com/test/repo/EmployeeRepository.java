package com.test.repo;

import com.test.dto.CustomeResponse;
import com.test.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findDataByName(String name);

    @Query(value = "select e.emp_id, e.name, e.age, e.active, e.designation,e.salary, e.phone_number " +
            "from Employee as e WHERE (e.name = :name OR e.age = :age OR e.designation = :designation OR e.active = :active)",nativeQuery = true)
    List<Employee> findDataBySearchEmployee(@Param("name") String name,
                                            @Param("age") Integer age,
                                            @Param("designation") String designation,
                                            @Param("active") Boolean active);

    @Query(value = "select e.emp_id, e.name, e.age, e.active, e.designation,e.salary, e.phone_number, a.city,a.state, a.country, a.pin_code from employee as e inner join address as a on e.emp_id=a.employee_id where e.emp_id = :empId OR a.pin_code= :pinCode",nativeQuery = true)
    List<Employee> findDataByQuery(@Param("empId") Long empId,
                                   @Param("pinCode") String pinCode);

    @Query("SELECT e.empId, e.name, e.age, e.active, e.designation,e.salary, e.phoneNumber, a.city,a.state, a.country, a.pinCode FROM Employee e JOIN e.addresses a WHERE a.city = :city")
    List<CustomeResponse> findEmployeesByCity(String city);
}
