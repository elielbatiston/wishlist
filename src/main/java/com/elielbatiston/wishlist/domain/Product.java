package com.elielbatiston.wishlist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {
	private String id;
	private String name;
	private Double price;
}
