package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.adapters.gateways.repositories.models.WishlistModel;
import com.elielbatiston.wishlist.domain.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishlistGateway {

    @Autowired
    private WishlistRepository repository;

    public Wishlist getWishlist(String idCustomer) {
        final WishlistModel model = repository.findByIdCustomer(idCustomer);
        return model == null ? null : model.toDomain();
    }

    public void save(Wishlist wishlist) {
        final WishlistModel model = WishlistModel.fromDomain(wishlist);
        repository.save(model);
    }
}
