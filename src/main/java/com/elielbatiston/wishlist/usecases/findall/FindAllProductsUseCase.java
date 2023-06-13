package com.elielbatiston.wishlist.usecases.findall;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.domains.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAllProductsUseCase {

    @Autowired
    private WishlistGatewayImpl gateway;

    public OutputFindAllProductsDTO execute(InputFindAllProductsDTO input) {
        final Wishlist whislist = gateway.getWishlist(input.idCustomer());
        return OutputFindAllProductsDTO.fromDomain(whislist);
    }
}
