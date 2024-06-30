package ru.selm.manager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.selm.manager.client.BadRequestException;
import ru.selm.manager.client.ProductRestClient;
import ru.selm.manager.controller.payload.NewProductPayload;
import ru.selm.manager.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductRestClient productRestClient;

    @InjectMocks
    ProductsController productsController;

    @Test
    void createProduct_RequestIsValid_ReturnsRedirectToProductPage() {
        //given
        NewProductPayload productPayload = new NewProductPayload("Товар 1", "Описание товара 1");
        ConcurrentModel model = new ConcurrentModel();

        doReturn(new Product(1, "Товар 1", "Описание товара 1"))
                .when(this.productRestClient)
                .createProduct("Товар 1", "Описание товара 1");

        //when
        String result = this.productsController.createProduct(productPayload, model);

        //then
        assertEquals("redirect:/catalog/products/1", result);

        verify(this.productRestClient).createProduct("Товар 1", "Описание товара 1");
        verifyNoMoreInteractions(this.productRestClient);
    }

    @Test
    void createProduct_RequestIsInvalid_ReturnsErrorPage() {
        //given
        NewProductPayload productPayload = new NewProductPayload("   ", null);
        ConcurrentModel model = new ConcurrentModel();

        List<String> errors = List.of("error1", "error2");
        doThrow(new BadRequestException(errors))
                .when(this.productRestClient)
                .createProduct("   ", null);
        //when
        String result = this.productsController.createProduct(productPayload, model);

        //then
        assertEquals("catalog/products/new_product", result);
        assertEquals(productPayload, model.getAttribute("payload"));
        assertEquals(errors, model.getAttribute("errors"));

        verify(this.productRestClient).createProduct("   ", null);
        verifyNoMoreInteractions(this.productRestClient);
    }
}