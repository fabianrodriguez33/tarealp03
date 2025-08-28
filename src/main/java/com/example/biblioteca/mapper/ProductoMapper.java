package com.example.biblioteca.mapper;

import com.example.biblioteca.dto.ProductoDTO;
import com.example.biblioteca.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductoMapper {

    @Mapping(source = "categoria.id", target = "categoriaId")  // Mapea la ID de la categoría al categoriaId del DTO
    ProductoDTO toDto(Producto producto);

    @Mapping(source = "categoriaId", target = "categoria.id")  // Mapea el categoriaId al campo categoria.id en la entidad
    Producto toEntity(ProductoDTO dto);

    List<ProductoDTO> toDtoList(List<Producto> productos);
}
