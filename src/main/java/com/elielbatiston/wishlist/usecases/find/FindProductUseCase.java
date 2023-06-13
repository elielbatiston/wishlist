package com.elielbatiston.wishlist.usecases.find;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindProductUseCase {

    @Autowired
    private WishlistGatewayImpl gateway;

    @Autowired
    private MessagesHelper messagesHelper;

    public OutputFindProductDTO execute(InputFindProductDTO input) {
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
        return new OutputFindProductDTO(
            OutputFindProductDTO.OutputFindAProductCustomerDTO.fromDomain(wishlist.getCustomer()),
            OutputFindProductDTO.OutputFindAProductProductDTO.fromDomain(product)
        );
    }
}
