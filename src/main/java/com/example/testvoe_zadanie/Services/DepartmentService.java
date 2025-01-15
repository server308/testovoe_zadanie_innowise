package com.example.testvoe_zadanie.Services;

import com.example.testvoe_zadanie.DTO.DepartmentDTO;
import com.example.testvoe_zadanie.Mappers.DepartmentMapper;
import com.example.testvoe_zadanie.Repositories.DepartmentRepository;
import com.example.testvoe_zadanie.models.Department;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;

@Service
public class DepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Transactional
    public DepartmentDTO createDepartment(@Valid DepartmentDTO departmentDTO) {
        log.info("Creating department from DTO");
        Department department = departmentMapper.fromDto(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department with id {} was created", savedDepartment.getId());
        return departmentMapper.toDto(savedDepartment);
    }

    @Transactional
    public DepartmentDTO updateDepartment(Long id, @Valid DepartmentDTO departmentDTO) {
        log.info("Updating department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department with id {} not found", id);
                    return new NoSuchElementException("Department with id " + id + " not found");
                });
        departmentMapper.updateFromDto(departmentDTO,department);
        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department with id {} was updated", updatedDepartment.getId());
        return departmentMapper.toDto(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        if (!departmentRepository.existsById(id)){
            log.error("Department with id {} not found", id);
            throw new NoSuchElementException("Department with id " + id + " not found");
        }
        departmentRepository.deleteById(id);
        log.info("Department with id {} was deleted", id);
    }
    public Page<DepartmentDTO> getAllDepartments( Pageable pageable) {
        log.info("Getting page of departments");
        return departmentRepository.findAll(pageable).map(departmentMapper::toDto);
    }
    public DepartmentDTO getDepartmentById(Long id) {
        log.info("Getting department with id {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department with id {} not found", id);
                    return new NoSuchElementException("Department with id " + id + " not found");
                });
        log.info("Department was found with id {}", department.getId());
        return departmentMapper.toDto(department);
    }
}
