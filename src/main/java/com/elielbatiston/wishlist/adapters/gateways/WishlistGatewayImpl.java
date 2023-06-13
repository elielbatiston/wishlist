package com.elielbatiston.wishlist.adapters.gateways;

import com.elielbatiston.wishlist.adapters.gateways.models.WishlistModel;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistGatewayImpl implements WishlistGateway {

    @Autowired
    private WishlistRepository repository;

    @Autowired
    private MessagesHelper messagesHelper;

    @Override
    public void save(final Wishlist wishlist) {
        final WishlistModel model = WishlistModel.fromDomain(wishlist);
        repository.save(model);
    }

    @Override
    public Wishlist getWishlist(final String idCustomer) {
        final WishlistModel model = repository.findByIdCustomer(idCustomer)
            .orElseThrow(() -> new ObjectNotFoundException(
                messagesHelper.getExceptionMessageObjectNotFound(idCustomer, Wishlist.class.getName())
            ));
        return model.toDomain();
    }

    @Override
    public void delete(final String id) {
        final ObjectId objId = new ObjectId(id);
        final WishlistModel model = repository.findById(objId)
            .orElseThrow(() -> new ObjectNotFoundException(
                messagesHelper.getExceptionMessageObjectNotFound(id, Wishlist.class.getName())
            ));
        repository.delete(model);
    }
}
