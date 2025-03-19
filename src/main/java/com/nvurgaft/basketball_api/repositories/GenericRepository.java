package com.nvurgaft.basketball_api.repositories;

import java.util.List;
import java.util.Optional;

/**
 * A generic repository interface to consolidate all required CRUD operations
 * needed from implementing repositories
 *
 * @param <T> Entity type
 * @param <ID> ID type
 */
public interface GenericRepository<T, ID> {
    int save(T entity);

    int saveAll(List<T> entities);

    int update(T entity);

    Optional<T> findById(ID id);

    int deleteById(ID id);

    List<T> findAll();

    int deleteAll();
}
