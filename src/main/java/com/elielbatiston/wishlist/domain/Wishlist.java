package com.elielbatiston.wishlist.domain;

import java.util.List;

public class Wishlist {
	private Customer customer;
	private List<Product> products;

	public Wishlist(Customer customer, List<Product> products) {
		this.customer = customer;
		this.products = products;
	}
}
