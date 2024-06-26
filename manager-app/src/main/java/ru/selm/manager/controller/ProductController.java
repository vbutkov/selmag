package ru.selm.manager.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.selm.manager.client.BadRequestException;
import ru.selm.manager.client.ProductRestClient;
import ru.selm.manager.controller.payload.UpdateProductPayload;
import ru.selm.manager.entity.Product;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalog/products/{productId:\\d+}")
public class ProductController {

    private final MessageSource messageSource;

    private final ProductRestClient productRestClient;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return productRestClient.findProduct(productId).orElseThrow(
                () -> new NoSuchElementException("catalog.errors.product.not_found"));
    }

    @GetMapping()
    public String getProduct() {
        return "/catalog/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "/catalog/products/edit_product";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product,
                                UpdateProductPayload payload, Model model) {
        try {
            this.productRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalog/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalog/products/edit_product";
        }
    }


    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/catalog/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {

        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", this.messageSource.getMessage(
                exception.getMessage(), new Object[0], exception.getMessage(), locale));
        return "errors/404";
    }

}