package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.usecases.dto.InputFindAProductDTO;
import com.elielbatiston.wishlist.usecases.dto.OutputFindAProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAProductInWishlistUseCase {

    @Autowired
    private WishlistGateway gateway;

    @Autowired
    private MessagesHelper messagesHelper;

    public OutputFindAProductDTO execute(InputFindAProductDTO input) {
        final Wishlist wishlist = gateway.getWishlist(input.idCustomer());
        final Product product = wishlist.getProducts().stream()
            .filter(it -> it.getId().equals(input.idProduct()))
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException(
                messagesHelper.getExceptionMessageObjectNotFound(
                    input.idProduct(),
                    Product.class.getName()
                )
            ));
        return new OutputFindAProductDTO(
            OutputFindAProductDTO.OutputFindAProductCustomerDTO.fromDomain(wishlist.getCustomer()),
            OutputFindAProductDTO.OutputFindAProductProductDTO.fromDomain(product)
        );
    }
}
