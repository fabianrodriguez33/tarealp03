package com.example.biblioteca.controller.general;

import com.example.biblioteca.controller.error.BusinessException;
import com.example.biblioteca.controller.error.ResourceNotFoundException;
import com.example.biblioteca.dto.ProductoDTO;
import com.example.biblioteca.service.general.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("api/v1/productos")
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ProductoDTO> productos = productoService.findAll();
            if (isNull(productos) || productos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(productos);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no existe"));
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        ProductoDTO nuevo = productoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        ProductoDTO actual = productoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));

        dto.setId(id);
        ProductoDTO actualizado = productoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        ProductoDTO producto = productoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe producto con id " + id));

        if ("I".equals(producto.getEstado())) {
            throw new BusinessException("El producto ya está inactivo");
        }

        productoService.deleteLogic(id);
        return ResponseEntity.ok("Producto desactivado correctamente");
    }
}
