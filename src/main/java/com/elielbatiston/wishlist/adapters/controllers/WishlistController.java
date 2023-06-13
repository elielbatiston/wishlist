package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.usecases.AddProductToWishlistUseCase;
import com.elielbatiston.wishlist.usecases.FindAProductInWishlistUseCase;
import com.elielbatiston.wishlist.usecases.FindAllCustomerProductsUseCase;
import com.elielbatiston.wishlist.usecases.RemoveProductFromWishlistUseCase;
import com.elielbatiston.wishlist.usecases.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value=WishlistController.WISHLIST_RESOURCE)
public class WishlistController {

    public static final String WISHLIST_RESOURCE = "/wishlist";
    private static final String WISHLIST_DELETE_A_PRODUCT_ENDPOINT = "/{idCustomer}/product/{idProduct}";
    private static final String WISHLIST_GET_WISHLIST_ENDPOINT = "/{idCustomer}";
    private static final String WISHLIST_GET_A_PRODUCT_IN_WISHLIST_ENDPOINT = "/{idCustomer}/{idProduct}";

    @Autowired
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @Autowired
    private RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @Autowired
    private FindAllCustomerProductsUseCase findAllCustomerProductsUseCase;

    @Autowired
    private FindAProductInWishlistUseCase findAProductInWishlistUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void wishlist(@Valid @RequestBody final InputAddProductToWishlistDTO dto) {
        addProductToWishlistUseCase.execute(dto);
    }

    @DeleteMapping(WISHLIST_DELETE_A_PRODUCT_ENDPOINT)
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

    @GetMapping(WISHLIST_GET_WISHLIST_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OutputFindAllCustomerProductsDTO> getWishlist(@PathVariable final String idCustomer) {
        final InputFindAllCustomerProductsDTO input = new InputFindAllCustomerProductsDTO(idCustomer);
        final OutputFindAllCustomerProductsDTO output = findAllCustomerProductsUseCase.execute(input);
        return ResponseEntity.ok().body(output);
    }

    @GetMapping(WISHLIST_GET_A_PRODUCT_IN_WISHLIST_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OutputFindAProductDTO> getAProduct(
        @PathVariable final String idCustomer,
        @PathVariable final String idProduct
    ) {
        final InputFindAProductDTO input = new InputFindAProductDTO(idCustomer, idProduct);
        final OutputFindAProductDTO output = findAProductInWishlistUseCase.execute(input);
        return ResponseEntity.ok().body(output);
    }
}
