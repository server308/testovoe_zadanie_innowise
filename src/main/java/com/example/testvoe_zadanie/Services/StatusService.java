package com.example.testvoe_zadanie.Services;


import com.example.testvoe_zadanie.models.Status;
import com.example.testvoe_zadanie.Repositories.StatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Status getStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Status not found"));
    }

    public Status createStatus(Status status) {
        return statusRepository.save(status);
    }

    public Status updateStatus(Long id, Status statusDetails) {
        Status status = getStatusById(id);
        status.setName(statusDetails.getName());
        return statusRepository.save(status);
    }

    public void deleteStatus(Long id) {
        Status status = getStatusById(id);
        statusRepository.delete(status);
    }
}