package com.example.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "TBL_CATEGORIA")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCATEGORIA")
    private Long id; // idcategoria

    @NotBlank(message = "La descripción no debe estar en blanco")
    @Size(min = 3, max = 50, message = "La descripción debe tener entre 3 y 50 caracteres")
    @Column(name = "DESCRIPCION", nullable = false, length = 50, unique = true)
    private String descripcion;

    @Column(name = "ESTADO", nullable = false, length = 1)
    private String estado = "A"; // 'A' por defecto para Activo

    // Relación uno a muchos con Producto
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Producto> productos;
}
