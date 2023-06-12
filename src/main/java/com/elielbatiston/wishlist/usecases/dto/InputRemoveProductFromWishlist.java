package com.elielbatiston.wishlist.usecases.dto;

public record InputRemoveProductFromWishlist (
    String idCustomer,
    String idProduct
) { }
