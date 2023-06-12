package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.adapters.gateways.repositories.models.WishlistModel;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.ports.WishlistRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistGateway implements WishlistRepositoryPort {

    @Autowired
    private WishlistRepository repository;

    @Autowired
    private MessagesHelper messagesHelper;

    public void save(final Wishlist wishlist) {
        final WishlistModel model = WishlistModel.fromDomain(wishlist);
        repository.save(model);
    }

    public Wishlist getWishlist(final String idCustomer) {
        final WishlistModel model = repository.findByIdCustomer(idCustomer)
            .orElseThrow(() -> new ObjectNotFoundException(
                messagesHelper.getExceptionMessageObjectNotFound(idCustomer, Wishlist.class.getName())
            ));
        return model.toDomain();
    }
}
