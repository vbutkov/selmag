package ru.selm.catalog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductsRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Sql("/sql/products.sql")
    @Test
    void findProducts_ReturnsProductsList() throws Exception {
        //give
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog-api/products")
                .param("filter", "товар")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalog")));
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                   {"id": 1, "title":  "Товар 1", "details": "Описание товара 1"},
                                   {"id": 3, "title":  "Товар 3", "details": "Описание товара 3"}
                                ]
                                """)
                );
    }

    @Test
    void createProduct_RequestIsInvalid_ReturnsProblemDetail() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/catalog-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title":  "",
                            "details": ""
                        }
                        """)
                .locale(new Locale("ru", "RU"))
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalog")));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                                {
                                    "errors": 
                                    [
                                        "Название товара должно быть от 3 до 25 символов"
                                    ]
                                 }""")
                );

    }

    @Test
    void createProduct_RequestIsValid_ReturnsNewProduct() throws Exception {
        //give
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/catalog-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title":  "Новый товар",
                            "details": "Описание нового товара"
                        }
                        """)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalog")));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/catalog-api/products/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "title":  "Новый товар",
                                     "details": "Описание нового товара"
                                 }""")
                );

    }

    @Test
    void createProduct_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        //give
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/catalog-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title":  "Новый товар",
                            "details": "Описание нового товара"
                        }
                        """)
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalog")));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );

    }

}