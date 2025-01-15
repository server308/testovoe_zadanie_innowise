package com.example.testvoe_zadanie.Services;

import com.example.testvoe_zadanie.DTO.ManagerDTO;
import com.example.testvoe_zadanie.Mappers.ManagerMapper;
import com.example.testvoe_zadanie.models.Manager;
import com.example.testvoe_zadanie.Repositories.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    public ManagerService(ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    public ManagerDTO createManager(ManagerDTO managerDTO) {
        Manager manager = managerMapper.toEntity(managerDTO);
        Manager savedManager = managerRepository.save(manager);
        return managerMapper.toDTO(savedManager);
    }

    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll()
                .stream()
                .map(managerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ManagerDTO getManagerById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id " + id));
        return managerMapper.toDTO(manager);
    }

    public ManagerDTO updateManager(Long id, ManagerDTO managerDTO) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id " + id));
        manager.setName(managerDTO.getName());
        Manager updatedManager = managerRepository.save(manager);
        return managerMapper.toDTO(updatedManager);
    }

    public void deleteManager(Long id) {
        if (!managerRepository.existsById(id)) {
            throw new RuntimeException("Manager not found with id " + id);
        }
        managerRepository.deleteById(id);
    }
}
