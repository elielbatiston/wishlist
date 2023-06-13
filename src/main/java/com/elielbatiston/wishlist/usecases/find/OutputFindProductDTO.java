package com.elielbatiston.wishlist.usecases.find;

import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;

public record OutputFindProductDTO(
    OutputFindAProductCustomerDTO customer,
    OutputFindAProductProductDTO product
) {

    public record OutputFindAProductCustomerDTO(
        String id,
        String name
    ) {

        public static OutputFindAProductCustomerDTO fromDomain(Customer customer) {
            return new OutputFindAProductCustomerDTO(
                customer.getId(),
                customer.getName()
            );
        }
    }

    public record OutputFindAProductProductDTO(
            String id,
            String name,
            Double price
    ) {

        public static OutputFindAProductProductDTO fromDomain(Product product) {
            return new OutputFindAProductProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
            );
        }
    }
}
