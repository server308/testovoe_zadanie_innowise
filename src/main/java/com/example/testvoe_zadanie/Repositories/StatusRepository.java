package com.example.testvoe_zadanie.Repositories;


import com.example.testvoe_zadanie.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
