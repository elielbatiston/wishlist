package com.elielbatiston.wishlist.domain;

public class Customer extends Entity {

    private String name;

    public Customer(String id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
