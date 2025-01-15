package com.example.testvoe_zadanie.Mappers;


import com.example.testvoe_zadanie.DTO.ManagerDTO;
import com.example.testvoe_zadanie.models.Manager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerMapper {
    Manager toEntity(ManagerDTO managerDTO);

    ManagerDTO toDTO(Manager manager);
}

