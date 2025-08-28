package com.example.biblioteca.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "{NotBlank.productoDTO.nombre}")
    @Size(min = 3, max = 60, message = "{Size.productoDTO.nombre}")
    private String nombre;

    @NotNull(message = "{NotNull.productoDTO.precio}")
    @DecimalMin(value = "0.00", inclusive = true, message = "{DecimalMin.productoDTO.precio}")
    @Digits(integer = 10, fraction = 2, message = "{Digits.productoDTO.precio}")
    private BigDecimal precio;

    @NotNull(message = "{NotNull.productoDTO.stock}")
    @Min(value = 0, message = "{Min.productoDTO.stock}")
    private Integer stock;

    @NotNull(message = "{NotNull.productoDTO.categoria}")
    private Long categoriaId;

    @NotBlank(message = "{NotBlank.productoDTO.estado}")
    @Pattern(regexp = "^[AI]$", message = "{Pattern.productoDTO.estado}")
    private String estado;
}
