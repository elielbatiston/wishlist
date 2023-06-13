package com.elielbatiston.wishlist.usecases.delete;

public record InputDeleteProductDTO(
    String idCustomer,
    String idProduct
) { }
