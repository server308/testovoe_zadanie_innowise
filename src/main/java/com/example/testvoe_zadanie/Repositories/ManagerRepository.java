package com.example.testvoe_zadanie.Repositories;


import com.example.testvoe_zadanie.models.Department;
import com.example.testvoe_zadanie.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findByName(String name);
}