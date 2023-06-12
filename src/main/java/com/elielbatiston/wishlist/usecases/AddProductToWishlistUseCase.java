package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddProductToWishlistUseCase {

    @Autowired
    private WishlistGateway gateway;

    public void execute(final InputAddProductToWishlistDTO dto) {
        Wishlist wishlist = gateway.getWishlist(dto.customer().id());
        if (wishlist == null) {
            final Customer customer = new Customer(
                dto.customer().id(),
                dto.customer().name()
            );
            wishlist = new Wishlist(customer);
        } else {
            wishlist.getCustomer().changeName(dto.customer().name());
        }
        wishlist.addProduct(dto.product().toDomain());
        gateway.save(wishlist);
    }
}
