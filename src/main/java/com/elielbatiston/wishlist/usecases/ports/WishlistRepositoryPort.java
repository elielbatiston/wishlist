package com.elielbatiston.wishlist.usecases.ports;

import com.elielbatiston.wishlist.domains.Wishlist;

public interface WishlistRepositoryPort {

    void save(final Wishlist wishlist);
}
