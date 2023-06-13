package com.elielbatiston.wishlist.usecases.remove;

public record InputRemoveProductDTO(
    String idCustomer,
    String idProduct
) { }
