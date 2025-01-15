package com.example.testvoe_zadanie.Repositories;

import com.example.testvoe_zadanie.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByEmployeeId(Long employeeId);
    void deleteByEmployeeId(Long employeeId);
}

