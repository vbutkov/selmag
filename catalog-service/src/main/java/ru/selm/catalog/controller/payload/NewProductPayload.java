package ru.selm.catalog.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(
        @NotNull(message = "{catalog.products.create.errors.title_is_invalid}")
        @Size(min = 3, max = 25, message = "{catalog.products.create.errors.title_size_is_invalid}")
        String title,
        @Size(max = 100, message = "{catalog.products.create.errors.details_size_are_invalid}")
        String details) {
}
