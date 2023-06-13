package com.elielbatiston.wishlist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
    WishlistProductsProperties.class
)
public class WishlistConfig {

    @Autowired
    private WishlistProductsProperties wishlistProductsProperties;

    public WishlistConfig(WishlistProductsProperties wishlistProductsProperties) {
        this.wishlistProductsProperties = wishlistProductsProperties;
    }

    public WishlistProductsProperties getWishlistProductsProperties() {
        return this.wishlistProductsProperties;
    }
}
