package com.service;

import java.util.List;

public interface IGeneralService<T> {
    List<T> findAll();

    T findById(Long id);

    void save(T t);

    void delete(Long id);

    List<T> findByName(String name);
}
