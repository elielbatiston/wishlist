package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.adapters.gateways.repositories.models.WishlistModel;
import com.elielbatiston.wishlist.domains.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishlistGateway {

    @Autowired
    private WishlistRepository repository;

    public void save(Wishlist wishlist) {
        final WishlistModel model = WishlistModel.fromDomain(wishlist);
        repository.save(model);
    }
}
