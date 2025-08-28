package com.example.biblioteca.service.general.impl;

import com.example.biblioteca.dto.ProductoDTO;
import com.example.biblioteca.entity.Producto;
import com.example.biblioteca.mapper.ProductoMapper;
import com.example.biblioteca.repository.ProductoRepository;
import com.example.biblioteca.service.general.service.ProductoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public List<ProductoDTO> findAll() throws ServiceException {
        try {
            List<Producto> list = productoRepository.findAll();
            return list.stream()
                    .map(productoMapper::toDto)  // Convierte Producto a ProductoDTO
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al obtener los productos", e);
        }
    }

    @Override
    public Optional<ProductoDTO> findById(long id) throws ServiceException {
        try {
            return productoRepository.findById(id).map(productoMapper::toDto);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener el producto con id: " + id, e);
        }
    }

    @Override
    public List<ProductoDTO> findByObject(ProductoDTO productoDTO) throws ServiceException {
        // Lógica para buscar productos por ejemplo por "nombre", "estado", "categoria", etc.
        try {
            Producto producto = productoMapper.toEntity(productoDTO);
            List<Producto> productos = productoRepository.findAll(); // Esto sería un ejemplo básico.
            return productos.stream()
                    .map(productoMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al buscar los productos", e);
        }
    }

    @Override
    public ProductoDTO save(ProductoDTO productoDTO) throws ServiceException {
        try {
            Producto producto = productoMapper.toEntity(productoDTO);
            Producto savedProducto = productoRepository.save(producto);
            return productoMapper.toDto(savedProducto);
        } catch (Exception e) {
            throw new ServiceException("Error al guardar el producto", e);
        }
    }

    @Override
    public ProductoDTO update(ProductoDTO productoDTO) throws ServiceException {
        try {
            Optional<Producto> producto = productoRepository.findById(productoDTO.getId());
            if (producto.isPresent()) {
                Producto prod = producto.get();
                prod.setNombre(productoDTO.getNombre());
                prod.setPrecio(productoDTO.getPrecio());
                prod.setStock(productoDTO.getStock());
                prod.setEstado(productoDTO.getEstado());
                Producto updatedProducto = productoRepository.save(prod);
                return productoMapper.toDto(updatedProducto);
            }
            throw new ServiceException("Producto no encontrado para actualizar.");
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el producto", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("El producto con id " + id + " no existe"));
            producto.setEstado("I"); // Cambiando el estado a 'Inactivo'
            productoRepository.save(producto);
        } catch (Exception e) {
            throw new ServiceException("Error al intentar eliminar lógicamente el producto con id " + id, e);
        }
    }
}
