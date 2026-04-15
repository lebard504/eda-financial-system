package com.devsu.financial.customer_service.domain.repositories;

import java.util.List;
import java.util.Optional;

public interface IBaseRepository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);
}

