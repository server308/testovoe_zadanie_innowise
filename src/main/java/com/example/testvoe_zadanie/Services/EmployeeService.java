package com.example.testvoe_zadanie.Services;
import com.example.testvoe_zadanie.DTO.EmployeeDTO;
import com.example.testvoe_zadanie.DTO.EmployeeResponseDTO;
import com.example.testvoe_zadanie.DTO.EmployeeSearchCriteria;
import com.example.testvoe_zadanie.DTO.EmployeeUpdateDTO;
import com.example.testvoe_zadanie.Repositories.DepartmentRepository;
import com.example.testvoe_zadanie.Repositories.EmployeeRepository;
import com.example.testvoe_zadanie.Specifications.EmployeeSpecification;
import com.example.testvoe_zadanie.models.Department;
import com.example.testvoe_zadanie.models.Employee;
import com.example.testvoe_zadanie.models.Status;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setPhotoUrl(employeeDTO.getPhotoUrl());
        employee.setManagerName(employeeDTO.getManagerName());

        Status status = Optional.ofNullable(employeeDTO.getStatus()).orElseThrow(() -> new EntityNotFoundException("Status not found"));
        employee.setStatus(status);
        employee.setDepartments(getDepartmentsFromNames(employeeDTO.getDepartments()));

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponseDTO(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, @Valid EmployeeUpdateDTO employeeUpdateDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found"));
        employee.setFirstName(employeeUpdateDTO.getFirstName());
        employee.setLastName(employeeUpdateDTO.getLastName());
        employee.setEmail(employeeUpdateDTO.getEmail());
        employee.setPhoneNumber(employeeUpdateDTO.getPhoneNumber());
        employee.setPhotoUrl(employeeUpdateDTO.getPhotoUrl());
        employee.setManagerName(employeeUpdateDTO.getManagerName());

        Status status = Optional.ofNullable(employeeUpdateDTO.getStatus()).orElseThrow(() -> new EntityNotFoundException("Status not found"));
        employee.setStatus(status);
        employee.setDepartments(getDepartmentsFromNames(employeeUpdateDTO.getDepartments()));
        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponseDTO(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found"));
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    public Page<EmployeeResponseDTO> getAllEmployees(EmployeeSearchCriteria criteria, Pageable pageable) {
        Specification<Employee> spec = EmployeeSpecification.getEmployeeSpecification(criteria);
        Page<Employee> employees = employeeRepository.findAll(spec, pageable);
        return employees.map(this::mapToResponseDTO);
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found"));
        if (employee.isDeleted()) {
            throw new EntityNotFoundException("Employee with id "+ id + "was deleted.");
        }
        return mapToResponseDTO(employee);
    }


    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();
        employeeResponseDTO.setId(employee.getId());
        employeeResponseDTO.setFirstName(employee.getFirstName());
        employeeResponseDTO.setLastName(employee.getLastName());
        employeeResponseDTO.setEmail(employee.getEmail());
        employeeResponseDTO.setPhoneNumber(employee.getPhoneNumber());
        employeeResponseDTO.setPhotoUrl(employee.getPhotoUrl());
        employeeResponseDTO.setStatus(employee.getStatus());
        employeeResponseDTO.setManagerName(employee.getManagerName());
        employeeResponseDTO.setDepartments(employee.getDepartments().stream().map(department -> {
            Department departmentResponse = new Department();
            departmentResponse.setId(department.getId());
            departmentResponse.setName(department.getName());
            return departmentResponse;}).collect(Collectors.toSet()));
        return employeeResponseDTO;
    }

    private Set<Department> getDepartmentsFromNames(Set<Department> departments) {
        if (departments == null || departments.isEmpty()) return null;
        return departments.stream()
                .map(department -> Optional.ofNullable(departmentRepository.findByName(department.getName()))
                        .orElseThrow(()-> new EntityNotFoundException("Department " + department.getName() + " not found."))).collect(Collectors.toSet());
    }
}
