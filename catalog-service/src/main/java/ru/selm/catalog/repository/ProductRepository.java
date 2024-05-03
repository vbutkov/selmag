package ru.selm.catalog.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.selm.catalog.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    //@Query(value = "select p from Product p where p.title ilike :filter")
    @Query(name = "Product.findAllByTitleLikeIgnoreCase", nativeQuery = true)
    Iterable<Product> findAllByTitleLikeIgnoreCase(@Param("filter") String filter);

}
