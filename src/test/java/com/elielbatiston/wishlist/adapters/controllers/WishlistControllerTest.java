package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.JsonUtil;
import com.elielbatiston.wishlist.adapters.controllers.exceptions.ControllerExceptionHandler;
import com.elielbatiston.wishlist.adapters.controllers.exceptions.ValidationError;
import com.elielbatiston.wishlist.config.InternationalizationConfig;
import com.elielbatiston.wishlist.usecases.AddProductToWishlistUseCase;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {
    WishlistController.class,
    ControllerExceptionHandler.class,
    InternationalizationConfig.class
})
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @Autowired
    private InternationalizationConfig config;

    @Test
    public void testCreate() throws Exception {
        final InputAddProductToWishlistDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
        mockMvc.perform(
            MockMvcRequestBuilders.post("/wishlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        assertDoesNotThrow(() -> verify(addProductToWishlistUseCase).execute(any()));
    }

    @Test
    public void testCreateWithoutPayload() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/wishlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(addProductToWishlistUseCase, never()).execute(any());
    }

    @Test
    public void testCreateWithCustomerAndProductLikeNullWithResponsePt_BR() throws Exception {
        Object obj = getDTO("mock/input_add_product_to_wishlist_wihout_customer_and_product.json");
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/wishlist")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj))
        )
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andReturn();
        verify(addProductToWishlistUseCase, never()).execute(any());

        ValidationError actual = getValidationError(result);
        assertEquals("Validation error", actual.getError());
        assertEquals(2, actual.getErrors().size());
        List<String> expectedFields = Arrays.asList("customer", "product");
        actual.getErrors().forEach(it -> {
            assertTrue(expectedFields.contains(it.getFieldName()));
            assertEquals(
                "Campo não pode ser nulo",
                it.getMessage()
            );
        });
    }

    @Test
    public void testCreateWithCustomerAndProductLikeNullWithResponseEn_US() throws Exception {
        Object obj = getDTO("mock/input_add_product_to_wishlist_wihout_customer_and_product.json");
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/wishlist")
                .header("Accept-Language", "en-US")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj))
        )
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andReturn();
        verify(addProductToWishlistUseCase, never()).execute(any());

        ValidationError actual = getValidationError(result);
        assertEquals("Validation error", actual.getError());
        assertEquals(2, actual.getErrors().size());
        List<String> expectedFields = Arrays.asList("customer", "product");
        actual.getErrors().forEach(it -> {
            assertTrue(expectedFields.contains(it.getFieldName()));
            assertEquals(
                "Field cannot be null",
                it.getMessage()
            );
        });
    }

    private InputAddProductToWishlistDTO getDTO(String path) {
        final JsonUtil<InputAddProductToWishlistDTO> jsonUtil = new JsonUtil<>(InputAddProductToWishlistDTO.class);
        return jsonUtil.read(path);
    }

    private ValidationError getValidationError(MvcResult result) throws JsonProcessingException {
        byte[] responseBytes = result.getResponse().getContentAsByteArray();
        String responseBody = new String(responseBytes, StandardCharsets.UTF_8);
        return objectMapper.readValue(responseBody, ValidationError.class);
    }
}