package com.example.testvoe_zadanie.Repositories;


import com.example.testvoe_zadanie.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}