package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.usecases.dto.InputFindAllCustomerProductsDTO;
import com.elielbatiston.wishlist.usecases.dto.OutputFindAllCustomerProductsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAllCustomerProductsUseCase {

    @Autowired
    private WishlistGateway gateway;

    public OutputFindAllCustomerProductsDTO execute(InputFindAllCustomerProductsDTO input) {
        final Wishlist whislist = gateway.getWishlist(input.idCustomer());
        return OutputFindAllCustomerProductsDTO.fromDomain(whislist);
    }
}
