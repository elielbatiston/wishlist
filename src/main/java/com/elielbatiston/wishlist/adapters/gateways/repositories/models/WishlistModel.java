package com.elielbatiston.wishlist.adapters.gateways.repositories.models;

import com.elielbatiston.wishlist.domain.Product;
import com.elielbatiston.wishlist.domain.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "wishlist")
@AllArgsConstructor
public class WishlistModel {

    @Id
    @Field("_id")
    private ObjectId id;

    @Indexed(unique = true)
    @Field("id_customer")
    private String idCustomer;

    private List<ProductModel> products;

    public Wishlist toDomain() {
        return new Wishlist(
            this.id.toString(),
            this.idCustomer,
            this.products.stream()
                .map(it -> it.toDomain())
                .collect(Collectors.toList())
        );
    }

    public static WishlistModel fromDomain(Wishlist wishlist) {
        return new WishlistModel(
           new ObjectId(wishlist.getId()),
           wishlist.getIdCustomer(),
           wishlist.getProducts().stream()
                .map(it -> ProductModel.fromDomain(it))
               .collect(Collectors.toList())
        );
    }

    @Getter
    @AllArgsConstructor
    public static class ProductModel {
        private String id;
        private String name;
        private Double price;

        public Product toDomain() {
            return new Product(
                this.id,
                this.name,
                this.price
            );
        }

        public static ProductModel fromDomain(Product product) {
            return new ProductModel(
                product.getId(),
                product.getName(),
                product.getPrice()
            );
        }
    }
}
