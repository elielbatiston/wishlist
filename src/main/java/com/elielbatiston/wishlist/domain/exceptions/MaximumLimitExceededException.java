package com.elielbatiston.wishlist.domain.exceptions;

public class MaximumLimitExceededException extends RuntimeException {

    public MaximumLimitExceededException(String msg) {
        super(msg);
    }
}
