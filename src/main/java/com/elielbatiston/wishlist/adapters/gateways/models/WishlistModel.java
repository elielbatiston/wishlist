package com.elielbatiston.wishlist.adapters.gateways.models;

import com.elielbatiston.wishlist.domains.Wishlist;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "wishlist")
public class WishlistModel {

    @Id
    @Field("_id")
    private ObjectId id;
    private CustomerModel customer;
    private Set<ProductModel> products;

    public WishlistModel(final ObjectId id, final CustomerModel customer, final Set<ProductModel> products) {
        this.id = id;
        this.customer = customer;
        this.products = products;
    }

    public Wishlist toDomain() {
        final Wishlist wishlist = new Wishlist(
            this.id.toString(),
            this.customer.toDomain()
        );
        this.products.stream()
            .forEach(it -> wishlist.addProduct(it.toDomain()));
        return wishlist;
    }

    public static WishlistModel fromDomain(final Wishlist wishlist) {
        final ObjectId id = wishlist.getId() == null ? null : new ObjectId(wishlist.getId());
        return new WishlistModel(
            id,
            CustomerModel.fromDomain(wishlist.getCustomer()),
            wishlist.getProducts().stream()
                .map(it -> ProductModel.fromDomain(it))
                .collect(Collectors.toSet())
        );
    }
}
