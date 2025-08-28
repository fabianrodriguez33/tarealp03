package com.example.biblioteca.repository;

import com.example.biblioteca.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Método para buscar por nombre de producto
    Optional<Producto> findByNombre(String nombre);

    // Método para buscar productos por estado (activo o inactivo)
    List<Producto> findByEstado(String estado);

    // Método para buscar productos por categoría
    List<Producto> findByCategoriaId(Long categoriaId);
}
