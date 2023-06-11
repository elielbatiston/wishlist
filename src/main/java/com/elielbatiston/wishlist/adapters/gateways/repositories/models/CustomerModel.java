package com.elielbatiston.wishlist.adapters.gateways.repositories.models;

import com.elielbatiston.wishlist.domain.Customer;
import org.springframework.data.mongodb.core.index.Indexed;

public class CustomerModel {

  @Indexed(unique = true)
  private String id;
  private String name;

  public CustomerModel(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public static CustomerModel fromDomain(Customer customer) {
    return new CustomerModel(
      customer.getId(),
      customer.getName()
    );
  }
}
