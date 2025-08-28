package com.example.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Long id;  // Campo para el ID, ya que en el DTO también puede ser necesario

    @NotBlank(message = "{NotBlank.categoriaDTO.desc}")
    @Size(min = 3, max = 50, message = "{Size.categoriaDTO.descripcion}")
    private String descripcion;  // Coincide con el nombre de la propiedad en la entidad

    @NotBlank(message = "{NotNull.categoriaDTO.estado}")
    private String estado;  // El estado, con los valores 'A' (Activo) o 'I' (Inactivo)
}
