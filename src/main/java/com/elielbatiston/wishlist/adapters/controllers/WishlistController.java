package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.usecases.AddProductToWishlistUseCase;
import com.elielbatiston.wishlist.usecases.RemoveProductFromWishlistUseCase;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import com.elielbatiston.wishlist.usecases.dto.InputRemoveProductFromWishlist;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController {

    private static final String WISHLIST_ENDPOINT = "/wishlist";
    private static final String WISHLIST_DELETE_A_PRODUCT_ENDPOINT = "/wishlist/{idCustomer}/product/{idProduct}";

    @Autowired
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @Autowired
    private RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @PostMapping
    @RequestMapping(WISHLIST_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public void wishlist(@Valid @RequestBody final InputAddProductToWishlistDTO dto) {
        addProductToWishlistUseCase.execute(dto);
    }

    @DeleteMapping
    @RequestMapping(WISHLIST_DELETE_A_PRODUCT_ENDPOINT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAProduct(
        @PathVariable final String idCustomer,
        @PathVariable final String idProduct
    ) {
        InputRemoveProductFromWishlist input = new InputRemoveProductFromWishlist(
                idCustomer,
                idProduct
        );
        removeProductFromWishlistUseCase.execute(input);
    }
}
