package com.elielbatiston.wishlist.usecases.remove;

public record InputDeleteProductDTO(
    String idCustomer,
    String idProduct
) { }
