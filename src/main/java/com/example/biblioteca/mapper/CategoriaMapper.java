package com.example.biblioteca.mapper;

import com.example.biblioteca.dto.CategoriaDTO;
import com.example.biblioteca.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoriaMapper {

    // Convierte la entidad Categoria a DTO
    CategoriaDTO toDto(Categoria categoria);

    // Convierte el DTO CategoriaDTO a entidad Categoria
    Categoria toEntity(CategoriaDTO dto);

    // Convierte una lista de entidades Categoria a una lista de DTOs CategoriaDTO
    List<CategoriaDTO> toDtoList(List<Categoria> categorias);
}
