package com.example.testvoe_zadanie.Repositories;


import com.example.testvoe_zadanie.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmail(String email);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}


