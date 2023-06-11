package com.elielbatiston.wishlist.adapters.gateways.repositories.models;

import com.elielbatiston.wishlist.domains.Product;

public class ProductModel {

  private String id;
  private String name;
  private Double price;

  public ProductModel(String id, String name, Double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public static ProductModel fromDomain(Product product) {
    return new ProductModel(
      product.getId(),
      product.getName(),
      product.getPrice()
    );
  }
}
