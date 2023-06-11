package com.elielbatiston.wishlist.domain;

import com.elielbatiston.wishlist.domain.exceptions.MaximumLimitExceededException;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Wishlist {
	private String id;
	private String idCustomer;
	private List<Product> products;

	public Wishlist(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public void addProduct(Product product) {
		if (this.products == null) {
			this.products = new ArrayList<>();
		}
		if (this.products.size() == 20) {
			throw new MaximumLimitExceededException("{exception.message.maximum.limit.exceeded}");
		}
		products.add(product);
	}
}
