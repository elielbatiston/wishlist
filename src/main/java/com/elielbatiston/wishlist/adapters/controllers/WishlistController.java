package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.usecases.AddProductToWishlistUseCase;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController {

    public static final String WISHLIST_ENDPOINT = "/wishlist";
    @Autowired
    private AddProductToWishlistUseCase usecase;

    @PostMapping
    @RequestMapping(WISHLIST_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public void wishlist(@Valid @RequestBody final InputAddProductToWishlistDTO dto) {
        usecase.execute(dto);
    }
}
