package com.elielbatiston.wishlist.domains.exceptions;

public class DataIntegrityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataIntegrityException(String msg) {
        super(msg);
    }
}
