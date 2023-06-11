package com.elielbatiston.wishlist.adapters.gateways.repositories.models;

import com.elielbatiston.wishlist.domains.Wishlist;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "wishlist")
public class WishlistModel {

    @Id
    @Field("_id")
    private ObjectId id;
    private CustomerModel customer;
    private List<ProductModel> products;

    public WishlistModel(ObjectId id, CustomerModel customer, List<ProductModel> products) {
        this.id = id;
        this.customer = customer;
        this.products = products;
    }

    public Wishlist toDomain() {
        return new Wishlist(
            this.id.toString(),
            this.customer.toDomain(),
            this.products.stream()
                .map(it -> it.toDomain())
                .collect(Collectors.toList())
        );
    }

    public static WishlistModel fromDomain(Wishlist wishlist) {
        final ObjectId id = wishlist.getId() == null ? null : new ObjectId(wishlist.getId());
        return new WishlistModel(
            id,
            CustomerModel.fromDomain(wishlist.getCustomer()),
            wishlist.getProducts().stream()
                .map(it -> ProductModel.fromDomain(it))
                .collect(Collectors.toList())
        );
    }
}
