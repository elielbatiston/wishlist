package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.controllers.dto.InputAddProductToWishlistDto;
import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domain.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddProductToWishlistUseCase {

    @Autowired
    private WishlistGateway gateway;

    public void execute(InputAddProductToWishlistDto dto) {
        final Wishlist wishlist = gateway.getWishlist(dto.getIdCustomer());
        wishlist.addProduct(dto.getProduct().toDomain());
        gateway.save(wishlist);
    }
}
