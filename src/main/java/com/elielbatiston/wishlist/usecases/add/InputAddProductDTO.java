package com.elielbatiston.wishlist.usecases.add;

import com.elielbatiston.wishlist.domains.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record InputAddProductDTO(
    @Valid
    @NotNull(message = "{validation.message.null}")
    CustomerDTO customer,

    @Valid
    @NotNull(message = "{validation.message.null}")
    ProductDTO product
) {

    public record CustomerDTO (
        @NotBlank(message="{validation.message.nullorblank}")
        String id,

        @Size(min=5, max=80, message="{validation.message.length.must.be.between}")
        @NotBlank(message="{validation.message.nullorblank}")
        String name
    ) {

    }

    public record ProductDTO (
        @NotBlank(message="{validation.message.nullorblank}")
        String id,

        @Size(min=5, max=80, message="{validation.message.length.must.be.between}")
        @NotBlank(message="{validation.message.nullorblank}")
        String name,

        @Positive(message="{validation.message.value.must.be.greater.than.zero}")
        Double price
    ) {

        public Product toDomain() {
            return new Product(
                this.id,
                this.name,
                this.price
            );
        }
    }
}
