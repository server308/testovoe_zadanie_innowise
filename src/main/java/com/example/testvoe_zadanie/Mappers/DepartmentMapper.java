package com.example.testvoe_zadanie.Mappers;


import com.example.testvoe_zadanie.DTO.DepartmentDTO;
import com.example.testvoe_zadanie.models.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    Department fromDto(DepartmentDTO departmentDTO);

    DepartmentDTO toDto(Department department);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(DepartmentDTO departmentDTO, @MappingTarget Department department);
}
