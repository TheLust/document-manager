package md.ceiti.frontend.service;

import java.util.List;

public interface CrudService<T> {
    // T request; G = response

    List<T> findAll();
    T insert(T request);
    T update(T request);
    void delete(T request);
}
