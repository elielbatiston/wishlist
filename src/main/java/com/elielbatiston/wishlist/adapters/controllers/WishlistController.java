package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.usecases.AddProductToWishlistUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.elielbatiston.wishlist.adapters.controllers.dto.InputAddProductToWishlistDto;
import jakarta.validation.Valid;

@RestController
public class WishlistController {	

	@Autowired
	private AddProductToWishlistUseCase addProductToWishlistUseCase;

	@PostMapping
	@RequestMapping("/wishlist")
	@ResponseStatus(HttpStatus.CREATED)
	public void wishlist(@Valid @RequestBody InputAddProductToWishlistDto dto) {
		addProductToWishlistUseCase.execute(dto);
	}
}
