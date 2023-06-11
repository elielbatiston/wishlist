package com.elielbatiston.wishlist.usecases.ports;

import com.elielbatiston.wishlist.domain.Wishlist;

public interface WishlistRepositoryPort {
    Wishlist getWishlist(String idCustomer);
}
