package ru.selm.catalog.repository.old;

import ru.selm.catalog.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryOld {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(Integer productId);

    void deleteById(Integer id);
}
