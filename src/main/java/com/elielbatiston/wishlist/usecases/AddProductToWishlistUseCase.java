package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddProductToWishlistUseCase {

    @Autowired
    private WishlistGateway gateway;

    public void execute(final InputAddProductToWishlistDTO input) {
        final Wishlist wishlist = this.getWishlistOrNew(input);
        wishlist.addProduct(input.product().toDomain());
        gateway.save(wishlist);
    }

    private Wishlist getWishlistOrNew(final InputAddProductToWishlistDTO input) {
        try {
            final Wishlist wishlist = gateway.getWishlist(input.customer().id());
            wishlist.getCustomer().changeName(input.customer().name());
            return wishlist;
        } catch (ObjectNotFoundException ex) {
            final Customer customer = new Customer(
                    input.customer().id(),
                    input.customer().name()
            );
            return new Wishlist(customer);
        }
    }
}
