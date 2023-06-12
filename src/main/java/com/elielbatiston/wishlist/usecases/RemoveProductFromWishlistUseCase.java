package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.dto.InputRemoveProductFromWishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveProductFromWishlistUseCase {

    @Autowired
    private WishlistGateway gateway;

    @Autowired
    private MessagesHelper messagesHelper;

    public void execute(final InputRemoveProductFromWishlist input) {
        final Wishlist wishlist = this.getWishlist(input);
        final Product product = wishlist.getProducts().stream()
            .filter(it -> it.getId().equals(input.idProduct()))
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException(
                messagesHelper.getExceptionMessageObjectNotFound(
                    input.idProduct(),
                    Product.class.getName()
                )
            ));
        wishlist.removeProduct(product);
        gateway.save(wishlist);
    }

    private Wishlist getWishlist(final InputRemoveProductFromWishlist input) {
        return gateway.getWishlist(input.idCustomer());
    }
}
