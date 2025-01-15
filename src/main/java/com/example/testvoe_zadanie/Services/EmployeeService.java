package com.example.testvoe_zadanie.Services;

import com.example.testvoe_zadanie.DTO.EmployeeDTO;
import com.example.testvoe_zadanie.DTO.EmployeeSearchCriteria;
import com.example.testvoe_zadanie.Mappers.EmployeeMapper;
import com.example.testvoe_zadanie.Repositories.*;
import com.example.testvoe_zadanie.models.*;
import jakarta.persistence.EntityNotFoundException;
import com.example.testvoe_zadanie.Specifications.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@Service
public class EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ManagerRepository managerRepository; // Добавить
    private final DepartmentRepository departmentRepository; // Добавить
    private final StatusRepository statusRepository;

    private final String PHOTO_DIRECTORY = "D:\\photos";  // Путь к папке для хранения фотографий

    public String getPHOTO_DIRECTORY() {
        return PHOTO_DIRECTORY;
    }

    public Path getPhotoPath(Long employeeId) {
        // Укажите директорию, где хранятся фотографии
        String directoryPath = "C:/uploads/photos/";
        String fileName = employeeId + "_photo.jpg";

        return Paths.get(directoryPath).resolve(fileName);
    }

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, ManagerRepository managerRepository, DepartmentRepository departmentRepository, StatusRepository statusRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.managerRepository = managerRepository;
        this.departmentRepository = departmentRepository;
        this.statusRepository = statusRepository;
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee");

        // Проверка на уникальность комбинации firstName + lastName
        if (employeeRepository.existsByFirstNameAndLastName(employeeDTO.getFirstName(), employeeDTO.getLastName())) {
            throw new IllegalArgumentException("Employee with the given first and last name already exists");
        }

        // Загружаем остальные данные, такие как статус, менеджер, департаменты
        Status status = statusRepository.findById(employeeDTO.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));

        Manager manager = managerRepository.findById(employeeDTO.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

        List<Department> departmentList = departmentRepository.findAllById(employeeDTO.getDepartmentIds());
        Set<Department> departments = new HashSet<>(departmentList);

        // Маппим DTO в сущность Employee
        Employee employee = employeeMapper.fromDto(employeeDTO);
        employee.setStatus(status);  // Устанавливаем статус
        employee.setManager(manager); // Устанавливаем менеджера
        employee.setDepartments(departments); // Устанавливаем департаменты

        // Сохраняем сотрудника
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created with id {}", savedEmployee.getId());
        return employeeMapper.toDto(savedEmployee);
    }


    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with id {}", id);

        // Загружаем сотрудника
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found"));

        // Обновляем данные сотрудника
        if (employeeDTO.getFirstName() != null) {
            employee.setFirstName(employeeDTO.getFirstName());
        }
        if (employeeDTO.getLastName() != null) {
            employee.setLastName(employeeDTO.getLastName());
        }
        if (employeeDTO.getEmail() != null) {
            employee.setEmail(employeeDTO.getEmail());
        }
        if (employeeDTO.getPhoneNumber() != null) {
            employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        }

        // Обновляем статус сотрудника
        if (employeeDTO.getStatusId() != null) {
            Status status = statusRepository.findById(employeeDTO.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found"));
            employee.setStatus(status);
        }

        // Обновляем менеджера
        if (employeeDTO.getManagerId() != null) {
            Manager manager = managerRepository.findById(employeeDTO.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found"));
            employee.setManager(manager);
        }

        // Обновляем департаменты
        if (employeeDTO.getDepartmentIds() != null) {
            List<Department> departmentList = departmentRepository.findAllById(employeeDTO.getDepartmentIds());
            Set<Department> departments = new HashSet<>(departmentList);
            employee.setDepartments(departments);
        }



        // Сохраняем обновленного сотрудника
        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee with id {} updated", id);

        // Возвращаем обновленные данные сотрудника
        return employeeMapper.toDto(updatedEmployee);
    }



    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee with id " + id + " not found");
        }
        employeeRepository.deleteById(id);
        log.info("Employee with id {} deleted", id);
    }

    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        log.info("Fetching employees");
        return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
    }

    @Transactional
    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found"));
        // Добавляем statusName в DTO
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        if (employee.getStatus() != null) {
            employeeDTO.setStatusName(employee.getStatus().getName());  // Заполняем статус
        }
        return employeeDTO;
    }

    @Transactional
    public Page<EmployeeDTO> searchEmployees(EmployeeSearchCriteria criteria, Pageable pageable) {
        // Создаем спецификацию на основе переданных критериев
        EmployeeSpecification specification = new EmployeeSpecification(criteria);

        // Получаем список сотрудников по спецификации
        Page<Employee> employees = employeeRepository.findAll(specification, pageable);

        // Маппим сущности в DTO и возвращаем результат
        return employees.map(employeeMapper::toDto);
    }






}