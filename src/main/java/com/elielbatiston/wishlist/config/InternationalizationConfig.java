package com.elielbatiston.wishlist.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class InternationalizationConfig extends ReloadableResourceBundleMessageSource {

	@Bean 
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = 
			new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	public String getInternationalizedMessage(String key, Object... parameters) {
		return messageSource().getMessage(key, parameters, null);
	}
}
