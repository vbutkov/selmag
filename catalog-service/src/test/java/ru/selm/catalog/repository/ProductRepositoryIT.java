package ru.selm.catalog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.selm.catalog.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/products.sql")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findAllByTitleLikeIgnoreCase_ReturnsFilteredProductList() {
        //given
        String filter = "%товар%";

        //when
        Iterable<Product> products = this.productRepository.findAllByTitleLikeIgnoreCase(filter);

        //then
        assertEquals(List.of(
                        new Product(1, "Товар 1", "Описание товара 1"),
                        new Product(3, "Товар 3", "Описание товара 3")
                ),
                products);

    }

}