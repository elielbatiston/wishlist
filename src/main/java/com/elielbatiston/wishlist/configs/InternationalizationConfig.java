package com.elielbatiston.wishlist.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import java.util.Locale;

@Configuration
public class InternationalizationConfig extends ReloadableResourceBundleMessageSource {

    private Locale locale;

    private final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

    @Bean
    public MessageSource messageSource() {
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    public String getInternationalizedMessage(final String key, final Object... parameters) {
        return messageSource().getMessage(key, parameters, this.locale);
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
        messageSource.setDefaultLocale(this.locale);
    }
}
