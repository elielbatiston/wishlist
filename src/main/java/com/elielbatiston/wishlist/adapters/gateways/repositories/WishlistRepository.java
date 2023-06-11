package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.adapters.gateways.repositories.models.WishlistModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

public interface WishlistRepository extends MongoRepository<WishlistModel, ObjectId> {
    @Query("{'customer.id': ?0}")
    WishlistModel findByIdCustomer(String idCustomer);
}
