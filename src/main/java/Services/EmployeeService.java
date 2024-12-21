package Services;

import Repositories.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import models.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees(Specification<Employee> spec, Pageable pageable) {
        return employeeRepository.findAll(spec, pageable).getContent();
    }


    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(id);
        BeanUtils.copyProperties(updatedEmployee, existingEmployee, "id", "createdAt");
        return employeeRepository.save(existingEmployee);
    }

    public void softDeleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }
}
