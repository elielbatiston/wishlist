package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.adapters.gateways.repositories.models.WishlistModel;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.usecases.ports.WishlistRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistGateway implements WishlistRepositoryPort {

    @Autowired
    private WishlistRepository repository;

    public void save(final Wishlist wishlist) {
        final WishlistModel model = WishlistModel.fromDomain(wishlist);
        repository.save(model);
    }

    public Wishlist getWishlist(final String idCustomer) {
        final WishlistModel model = repository.findByIdCustomer(idCustomer);
        return model == null ? null : model.toDomain();
    }
}
