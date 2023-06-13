package com.elielbatiston.wishlist.configs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InternationalizationConfigTest {

    @InjectMocks
    private InternationalizationConfig config;

    @Test
    public void testPt_BRMessage() {
        String message = config.getInternationalizedMessage("validation.message.value.must.be.greater.than.zero");
        assertEquals("O valor deve ser maior que zero", message);
    }

    @Test
    public void testEn_USMessage() {
        config.setLocale(Locale.US);
        String message = config.getInternationalizedMessage("validation.message.value.must.be.greater.than.zero");
        assertEquals("The value must be greater than zero", message);
    }
}
