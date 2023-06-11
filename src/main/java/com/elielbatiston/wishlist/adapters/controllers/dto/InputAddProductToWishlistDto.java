package com.elielbatiston.wishlist.adapters.controllers.dto;

import com.elielbatiston.wishlist.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InputAddProductToWishlistDto {
	@JsonProperty("id_customer")
	@NotBlank(message="{validation.message.nullorblank}")
	private String idCustomer;
	
	@JsonProperty("product")
	@Valid
	@NotNull(message = "{validation.message.null}")
	private ProductDto product;
		
	@Getter
	public static class ProductDto {
		@NotBlank(message="{validation.message.nullorblank}")
		private String id;
		
		@Size(min=5, max=80, message="{validation.message.length.must.be.between}")
		@NotBlank(message="{validation.message.nullorblank}")
		private String name;
		
		@Positive(message="{validation.message.value.must.be.greater.than.zero}")
		private Double price;

		public Product toDomain() {
			return new Product(
				this.id,
				this.name,
				this.price
			);
		}
	}
}
