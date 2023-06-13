package com.elielbatiston.wishlist.adapters.gateways;

import com.elielbatiston.wishlist.domains.Wishlist;

public interface WishlistGateway {

    void save(final Wishlist wishlist);
    void delete(final String id);
    Wishlist getWishlist(final String idCustomer);
}
