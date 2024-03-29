package com.elielbatiston.wishlist.usecases.delete;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteProductUseCase {

    @Autowired
    private WishlistGatewayImpl gateway;

    @Autowired
    private MessagesHelper messagesHelper;

    public void execute(final InputDeleteProductDTO input) {
        final Wishlist wishlist = gateway.getWishlist(input.idCustomer());
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

        if (wishlist.getProducts().size() == 0) {
            gateway.delete(wishlist.getId());
        }
    }
}
