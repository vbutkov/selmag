package ru.selm.manager.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.selm.manager.client.BadRequestException;
import ru.selm.manager.client.ProductRestClient;
import ru.selm.manager.controller.payload.NewProductPayload;
import ru.selm.manager.entity.Product;

import java.security.Principal;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalog/products")
public class ProductsController {


    private final ProductRestClient productRestClient;

    @GetMapping("list")
    public String getProductsList(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.productRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalog/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalog/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload, Model model) {
        try {
            Product product = this.productRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalog/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalog/products/new_product";
        }
    }

}