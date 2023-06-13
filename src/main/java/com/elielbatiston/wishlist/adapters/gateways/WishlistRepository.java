package com.elielbatiston.wishlist.adapters.gateways;

import com.elielbatiston.wishlist.adapters.gateways.models.WishlistModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;

public interface WishlistRepository extends MongoRepository<WishlistModel, ObjectId> {
    @Query("{'customer.id': ?0}")
    Optional<WishlistModel> findByIdCustomer(final String idCustomer);
}
