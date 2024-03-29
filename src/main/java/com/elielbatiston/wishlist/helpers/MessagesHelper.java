package com.elielbatiston.wishlist.helpers;

import com.elielbatiston.wishlist.configs.InternationalizationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagesHelper {

    private static final String EXCEPTION_MESSAGE_OBJECT_NOT_FOUND = "exception.message.object.not.found";
    private static final String EXCEPTION_NOT_FOUND_MESSAGE_HEADER = "exception.not.found.message.header";
    private static final String EXCEPTION_VALIDATION_MESSAGE_HEADER = "exception.validation.message.header";
    private static final String EXCEPTION_MESSAGE_MAXIMUM_PRODUCT_LIMIT_EXCEEDED = "exception.message.maximum.product.limit.exceeded";

    @Autowired
    private InternationalizationConfig config;

    public String getExceptionMessageObjectNotFound(String id, String clazz) {
        String message = config.getInternationalizedMessage(EXCEPTION_MESSAGE_OBJECT_NOT_FOUND);
        return String.format(message, id, clazz);
    }

    public String getExceptionNotFoundMessageHeader() {
        return config.getInternationalizedMessage(EXCEPTION_NOT_FOUND_MESSAGE_HEADER);
    }

    public String getExceptionValidationMessageHeader() {
        return config.getInternationalizedMessage(EXCEPTION_VALIDATION_MESSAGE_HEADER);
    }

    public String getExceptionMaximumProductLimitExceeded(Integer maximumLimitAllowed) {
        String message = config.getInternationalizedMessage(EXCEPTION_MESSAGE_MAXIMUM_PRODUCT_LIMIT_EXCEEDED);
        return String.format(message, maximumLimitAllowed);
    }
}
