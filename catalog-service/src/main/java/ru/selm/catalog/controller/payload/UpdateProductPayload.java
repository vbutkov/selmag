package ru.selm.catalog.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductPayload(
        @NotNull(message = "{catalog.products.update.errors.title_is_invalid}")
        @Size(min = 3, max = 25, message = "{catalog.products.update.errors.title_size_is_invalid}")
        String title,
        @Size(max = 100, message = "{catalog.products.update.errors.details_size_is_invalid}")
        String details) {
}
