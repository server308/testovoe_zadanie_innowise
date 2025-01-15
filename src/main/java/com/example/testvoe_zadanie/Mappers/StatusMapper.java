package com.example.testvoe_zadanie.Mappers;


import com.example.testvoe_zadanie.DTO.StatusDTO;
import com.example.testvoe_zadanie.models.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusDTO toDto(Status status);
    Status fromDto(StatusDTO statusDTO);
}
