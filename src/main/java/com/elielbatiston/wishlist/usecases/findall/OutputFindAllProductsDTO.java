package com.elielbatiston.wishlist.usecases.findall;

import com.elielbatiston.wishlist.domains.Wishlist;

import java.util.List;
import java.util.stream.Collectors;

public record OutputFindAllProductsDTO(
    OutputFindAllCustomerProductCustomerDTO customer,
    List<OutputFindAllCustomerProductProductDTO> products
) {

    public static OutputFindAllProductsDTO fromDomain(Wishlist wishlist) {
        final OutputFindAllCustomerProductCustomerDTO customer = new OutputFindAllCustomerProductCustomerDTO(
            wishlist.getCustomer().getId(),
            wishlist.getCustomer().getName()
        );
        final List<OutputFindAllCustomerProductProductDTO> products = wishlist.getProducts().stream()
            .map(it -> new OutputFindAllCustomerProductProductDTO(
                it.getId(),
                it.getName(),
                it.getPrice()
            ))
            .collect(Collectors.toList());

        return new OutputFindAllProductsDTO(
            customer,
            products
        );
    }

    public record OutputFindAllCustomerProductCustomerDTO (
        String id,
        String name
    ) { }

    public record OutputFindAllCustomerProductProductDTO (
        String id,
        String name,
        Double price
    ) { }
}
