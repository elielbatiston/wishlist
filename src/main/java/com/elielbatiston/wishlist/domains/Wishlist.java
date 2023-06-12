package com.elielbatiston.wishlist.domains;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends Entity {

	private Customer customer;
	private List<Product> products;

	public Wishlist(final String id, final Customer customer) {
		super(id);
		this.customer = customer;
	}

	public Wishlist(final Customer customer) {
		this(null, customer);
	}

	public Customer getCustomer() {
		return customer;
	}

	public List<Product> getProducts() {
		if (this.products == null) {
			this.products = new ArrayList<>();
		}
		return this.products;
	}

	public void addProduct(Product product) {
		if (this.products == null) {
			this.products = new ArrayList<>();
		}
		this.products.add(product);
	}

	public void removeProduct(Product product) {
		this.getProducts().remove(product);
	}
}
