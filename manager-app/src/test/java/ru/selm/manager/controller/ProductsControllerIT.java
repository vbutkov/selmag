package ru.selm.manager.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.selm.manager.entity.Product;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 54321)
//@ActiveProfiles(value = "standalone")
class ProductsControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getProductList_ReturnsProductListPage() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/products/list")
                .queryParam("filter", "товар")
                .with(user("user2").roles("MANAGER"));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/catalog-api/products"))
                .withQueryParam("filter", WireMock.equalTo("товар")).willReturn(WireMock.ok("""
                        [
                            {"id":1, "title": "товар 1", "details": "описание товара 1"},
                            {"id":2, "title": "товар 2", "details": "описание товара 2"}
                        ]
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));


        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print()).
                andExpectAll(
                        status().isOk(),
                        view().name("catalog/products/list"),
                        model().attribute("filter", "товар"),
                        model().attribute("products",
                                List.of(new Product(1, "товар 1", "описание товара 1"),
                                        new Product(2, "товар 2", "описание товара 2"))));

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathMatching("/catalog-api/products"))
                .withQueryParam("filter", WireMock.equalTo("товар")));
    }

    @Test
    void getNewProductPage_ReturnsProductPage() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/products/create")
                .with(user("user2").roles("MANAGER"));
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print()).andExpectAll(status().isOk(), view().name("catalog/products/new_product"));

    }
}
