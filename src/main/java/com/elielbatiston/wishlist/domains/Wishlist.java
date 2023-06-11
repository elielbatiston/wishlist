package com.elielbatiston.wishlist.domains;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends Entity {

	private Customer customer;
	private List<Product> products;

	public Wishlist(final String id, final Customer customer, List<Product> products) {
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

	public Wishlist(final Customer customer) {
		this(null, customer, new ArrayList<>());
	}

	public void addProduct(Product product) {
		if (this.products == null) {
			this.products = new ArrayList<>();
		}
		this.products.add(product);
	}
}
