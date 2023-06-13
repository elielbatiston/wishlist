package com.elielbatiston.wishlist.adapters.gateways.models;

import com.elielbatiston.wishlist.domains.Product;

import java.util.Objects;

public class ProductModel {

    private String id;
    private String name;
    private Double price;

    public ProductModel(String id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product toDomain() {
        return new Product(
            this.id,
            this.name,
            this.price
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductModel that = (ProductModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static ProductModel fromDomain(Product product) {
        return new ProductModel(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
    }
}
