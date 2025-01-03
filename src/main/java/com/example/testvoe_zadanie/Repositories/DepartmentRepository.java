package com.example.testvoe_zadanie.Repositories;

import com.example.testvoe_zadanie.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Department findByName(String name);

}

