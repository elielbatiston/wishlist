package com.elielbatiston.wishlist.domains;

import java.util.List;

public class Wishlist extends Entity {

	private Customer customer;
	private List<Product> products;

	public Wishlist(String id, Customer customer, List<Product> products) {
		super(id);
		this.customer = customer;
		this.products = products;
	}

	public Customer getCustomer() {
		return customer;
	}

	public List<Product> getProducts() {
		return products;
	}
}
