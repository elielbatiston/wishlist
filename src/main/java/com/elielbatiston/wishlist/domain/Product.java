package com.elielbatiston.wishlist.domain;

public class Product extends Entity {

	private String name;
	private Double price;

	public Product(String id, String name, Double price) {
		super(id);
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}
}
