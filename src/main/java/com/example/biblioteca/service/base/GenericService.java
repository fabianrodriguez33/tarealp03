package com.example.biblioteca.service.base;

import org.hibernate.service.spi.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {
    List<T> findAll() throws ServiceException;
    Optional<T> findById(long id) throws ServiceException;
    List<T> findByObject(T t) throws ServiceException;
    T save(T t) throws ServiceException;
    T update(T t) throws ServiceException;
    void deleteLogic(Long id) throws ServiceException;
}
