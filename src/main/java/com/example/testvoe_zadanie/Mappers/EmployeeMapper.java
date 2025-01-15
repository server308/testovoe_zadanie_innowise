package com.example.testvoe_zadanie.Mappers;

import com.example.testvoe_zadanie.DTO.EmployeeDTO;
import com.example.testvoe_zadanie.models.Department;
import com.example.testvoe_zadanie.models.Employee;
import com.example.testvoe_zadanie.models.Manager;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {StatusMapper.class, DepartmentMapper.class, ManagerMapper.class}
)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    // Маппинг Employee -> EmployeeDTO
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", source = "manager.name") // Используем имя из Manager
    @Mapping(target = "departmentIds", source = "departments", qualifiedByName = "mapDepartmentsToIds")
    @Mapping(target = "departmentNames", source = "departments", qualifiedByName = "mapDepartmentsToNames")
    EmployeeDTO toDto(Employee employee);

    // Маппинг EmployeeDTO -> Employee
    @Mapping(target = "status", ignore = true) // Требует ручного назначения
    @Mapping(target = "manager", ignore = true) // Требует ручного назначения
    @Mapping(target = "departments", ignore = true) // Требует ручного назначения
    Employee fromDto(EmployeeDTO employeeDTO);

    // Обновление существующего объекта Employee из DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departments", ignore = true)
    void updateFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

    // Маппинг Set<Department> -> Set<Long> (IDs)
    @Named("mapDepartmentsToIds")
    static Set<Long> mapDepartmentsToIds(Set<Department> departments) {
        if (departments == null) return null;
        return departments.stream()
                .map(Department::getId)
                .collect(Collectors.toSet());
    }

    // Маппинг Set<Department> -> Set<String> (Names)
    @Named("mapDepartmentsToNames")
    static Set<String> mapDepartmentsToNames(Set<Department> departments) {
        if (departments == null) return null;
        return departments.stream()
                .map(Department::getName)
                .collect(Collectors.toSet());
    }

    default EmployeeDTO toDtoWithStatusName(Employee employee) {
        EmployeeDTO employeeDTO = toDto(employee);
        if (employee.getStatus() != null) {
            employeeDTO.setStatusName(employee.getStatus().getName()); // Устанавливаем статус
        }
        return employeeDTO;
    }
}
