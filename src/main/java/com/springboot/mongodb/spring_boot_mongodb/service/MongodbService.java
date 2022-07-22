package com.springboot.mongodb.spring_boot_mongodb.service;

import java.util.List;

public interface MongodbService<T> {
    boolean save(T entity);

    T update(T entity);

    T findById(long id);

    T findByName(String name);

    List<T> findAll();

    boolean deleteById(long id);

    boolean deleteByName(String name);

    void deleteAll();
}