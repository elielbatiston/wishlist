package com.elielbatiston.wishlist.usecases.add;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.configs.untestable.WishlistConfig;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.DataIntegrityException;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddProductUseCase {

    @Autowired
    private WishlistConfig config;

    @Autowired
    private WishlistGatewayImpl gateway;

    @Autowired
    private MessagesHelper messagesHelper;

    public void execute(final InputAddProductDTO input) {
        final Integer maximumLimitAllowed = config.getWishlistProductsProperties().getMaximumLimitAllowed();
        final Wishlist wishlist = this.getWishlistOrNew(input);
        wishlist.addProduct(input.product().toDomain());
        if (wishlist.getProducts().size() > maximumLimitAllowed) {
            throw new DataIntegrityException(messagesHelper.getExceptionMaximumProductLimitExceeded(maximumLimitAllowed));
        }
        gateway.save(wishlist);
    }

    private Wishlist getWishlistOrNew(final InputAddProductDTO input) {
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
