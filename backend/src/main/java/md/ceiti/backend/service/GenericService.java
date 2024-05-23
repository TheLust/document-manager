package md.ceiti.backend.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenericService<T, G> {

    List<T> findAll();
    T getById(G id);

    @Transactional
    T insert(T entity);

    @Transactional
    T update(T presentEntity, T updatedEntity);

    @Transactional
    void delete(T entity);

    @Transactional
    void deleteById(G id);
}
