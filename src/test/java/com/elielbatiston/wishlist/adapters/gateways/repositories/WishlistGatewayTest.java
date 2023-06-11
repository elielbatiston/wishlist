package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.domain.Customer;
import com.elielbatiston.wishlist.domain.Product;
import com.elielbatiston.wishlist.domain.Wishlist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class WishlistGatewayTest {
  @Mock
  private WishlistRepository repository;

  @InjectMocks
  private WishlistGateway gateway;

  @Test
  public void save() {
    Wishlist wishlist = new Wishlist(
      "123456789012345678901234",
      new Customer(
        "C1",
        "Nome 1"
      ),
      Arrays.asList(
        new Product(
          "P1",
          "Produto 1",
          99.5
        )
      )
    );

    gateway.save(wishlist);
    verify(repository).save(any());
  }
}