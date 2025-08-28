package com.example.biblioteca.service.general.impl;

import com.example.biblioteca.dto.CategoriaDTO;
import com.example.biblioteca.entity.Categoria;
import com.example.biblioteca.mapper.CategoriaMapper;
import com.example.biblioteca.repository.CategoriaRepository;
import com.example.biblioteca.service.general.service.CategoriaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public List<CategoriaDTO> findAll() throws ServiceException {
        try {
            List<Categoria> list = categoriaRepository.findAll();
            return list.stream()
                    .map(categoriaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al obtener las categorías", e);
        }
    }

    @Override
    public Optional<CategoriaDTO> findById(long id) throws ServiceException {
        try {
            return categoriaRepository.findById(id).map(categoriaMapper::toDto);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener la categoría con id: " + id, e);
        }
    }

    @Override
    public List<CategoriaDTO> findByObject(CategoriaDTO categoriaDTO) throws ServiceException {
        // Si quieres filtrar las categorías por ciertos parámetros, puedes hacerlo aquí
        // por ejemplo, por nombre o estado:
        try {
            // Lógica para filtrar por nombre o estado si lo deseas
            Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
            List<Categoria> categorias = categoriaRepository.findAll(); // Esto sería un ejemplo básico.
            return categorias.stream()
                    .map(categoriaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al buscar las categorías", e);
        }
    }

    @Override
    public CategoriaDTO save(CategoriaDTO categoriaDTO) throws ServiceException {
        try {
            Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
            Categoria savedCategoria = categoriaRepository.save(categoria);
            return categoriaMapper.toDto(savedCategoria);
        } catch (Exception e) {
            throw new ServiceException("Error al guardar la categoría", e);
        }
    }

    @Override
    public CategoriaDTO update(CategoriaDTO categoriaDTO) throws ServiceException {
        try {
            Optional<Categoria> categoria = categoriaRepository.findById(categoriaDTO.getId());
            if (categoria.isPresent()) {
                Categoria cat = categoria.get();
                cat.setDescripcion(categoriaDTO.getDescripcion());
                cat.setEstado(categoriaDTO.getEstado());
                Categoria updatedCategoria = categoriaRepository.save(cat);
                return categoriaMapper.toDto(updatedCategoria);
            }
            throw new ServiceException("Categoría no encontrada para actualizar.");
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la categoría", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("La categoría con id " + id + " no existe"));
            categoria.setEstado("I"); // Cambiando el estado a 'Inactivo'
            categoriaRepository.save(categoria);
        } catch (Exception e) {
            throw new ServiceException("Error al intentar eliminar lógicamente la categoría con id " + id, e);
        }
    }
}
