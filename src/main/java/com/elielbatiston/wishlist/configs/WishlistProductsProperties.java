package com.elielbatiston.wishlist.configs;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wishlist.products")
public class WishlistProductsProperties {
    @NotBlank
    private Integer maximumLimitAllowed;

    public WishlistProductsProperties(Integer maximumLimitAllowed) {
        this.maximumLimitAllowed = maximumLimitAllowed;
    }

    public Integer getMaximumLimitAllowed() {
        return maximumLimitAllowed;
    }
}
