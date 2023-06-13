package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.JsonUtil;
import com.elielbatiston.wishlist.adapters.controllers.exceptions.ControllerExceptionHandler;
import com.elielbatiston.wishlist.adapters.controllers.exceptions.ValidationError;
import com.elielbatiston.wishlist.config.ApplicationConfig;
import com.elielbatiston.wishlist.config.InterceptorHandle;
import com.elielbatiston.wishlist.config.InternationalizationConfig;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.AddProductToWishlistUseCase;
import com.elielbatiston.wishlist.usecases.FindAllCustomerProductsUseCase;
import com.elielbatiston.wishlist.usecases.RemoveProductFromWishlistUseCase;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import com.elielbatiston.wishlist.usecases.dto.InputFindAllCustomerProductsDTO;
import com.elielbatiston.wishlist.usecases.dto.InputRemoveProductFromWishlist;
import com.elielbatiston.wishlist.usecases.dto.OutputFindAllCustomerProductsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
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

import java.io.UnsupportedEncodingException;
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
    InternationalizationConfig.class,
    MessagesHelper.class,
    ApplicationConfig.class,
    InterceptorHandle.class
})
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @MockBean
    private RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @MockBean
    private FindAllCustomerProductsUseCase findAllCustomerProductsUseCase;

    @Autowired
    private InternationalizationConfig config;

    @Nested
    class Create {
        @Test
        public void testCreate() throws Exception {
            final InputAddProductToWishlistDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
            mockMvc.perform(
                MockMvcRequestBuilders.post("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
                    .content(objectMapper.writeValueAsString(dto))
            ).andExpect(MockMvcResultMatchers.status().isCreated());
            assertDoesNotThrow(() -> verify(addProductToWishlistUseCase).execute(any()));
        }

        @Test
        public void testCreateWithoutPayload() throws Exception {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
                    .content(objectMapper.writeValueAsString(null))
            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
            verify(addProductToWishlistUseCase, never()).execute(any());
        }

        @Test
        public void testCreateWithCustomerAndProductLikeNullWithResponsePt_BR() throws Exception {
            final Object obj = getDTO("mock/input_add_product_to_wishlist_wihout_customer_and_product.json");
            final MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/wishlist")
                    .header("Accept-Language", "pt_BR")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(obj))
            )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();
            verify(addProductToWishlistUseCase, never()).execute(any());

            final ValidationError actual = getValidationError(result);
            assertEquals("Erro de validação", actual.getError());
            assertEquals(2, actual.getErrors().size());
            final List<String> expectedFields = Arrays.asList("customer", "product");
            actual.getErrors().forEach(it -> {
                assertTrue(expectedFields.contains(it.getFieldName()));
                assertEquals(
                        "Campo não pode ser nulo",
                        it.getMessage()
                );
            });
        }

        private InputAddProductToWishlistDTO getDTO(String path) {
            final JsonUtil<InputAddProductToWishlistDTO> jsonUtil = new JsonUtil<>(InputAddProductToWishlistDTO.class);
            return jsonUtil.read(path);
        }

        private ValidationError getValidationError(MvcResult result) throws JsonProcessingException {
            final byte[] responseBytes = result.getResponse().getContentAsByteArray();
            final String responseBody = new String(responseBytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(responseBody, ValidationError.class);
        }
    }

    @Nested
    class DeleteAProduct {
        @Test
        public void testDelete() throws Exception {
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/wishlist/C1/product/P1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
            ).andExpect(MockMvcResultMatchers.status().isNoContent());
            assertDoesNotThrow(() -> verify(removeProductFromWishlistUseCase).execute(any()));
        }

        @Test
        public void testDeleteShouldThrowObjectNotFoundException() throws Exception {
            doThrow(ObjectNotFoundException.class)
                .when(removeProductFromWishlistUseCase)
                .execute(any(InputRemoveProductFromWishlist.class));
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/wishlist/C1/product/P1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound());
            verify(removeProductFromWishlistUseCase).execute(any());
        }
    }

    @Nested
    class GetWishlist {
        @Test
        public void testGetWishlist() throws Exception {
            final OutputFindAllCustomerProductsDTO output = getOutputFindAllCustomerProductsDTO();
            when(findAllCustomerProductsUseCase.execute(any())).thenReturn(output);
            final MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/wishlist/C1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
            )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
            assertDoesNotThrow(() -> verify(findAllCustomerProductsUseCase).execute(any()));
            final OutputFindAllCustomerProductsDTO actual = getOutputFindAllCustomerProductsDTO(result);
            assertInstanceOf(OutputFindAllCustomerProductsDTO.class, actual);
            assertEquals(output.customer().id(), actual.customer().id());
            assertEquals(output.customer().name(), actual.customer().name());
            assertEquals(2, actual.products().size());
            assertEquals(output.products().get(0).id(), actual.products().get(0).id());
            assertEquals(output.products().get(0).name(), actual.products().get(0).name());
            assertEquals(output.products().get(1).price(), actual.products().get(1).price());
            assertEquals(output.products().get(1).id(), actual.products().get(1).id());
            assertEquals(output.products().get(1).name(), actual.products().get(1).name());
        }

        @Test
        public void testDeleteShouldThrowObjectNotFoundException() throws Exception {
            doThrow(ObjectNotFoundException.class)
                .when(findAllCustomerProductsUseCase)
                .execute(any(InputFindAllCustomerProductsDTO.class));
            mockMvc.perform(
                MockMvcRequestBuilders.get("/wishlist/C1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound());
            verify(findAllCustomerProductsUseCase).execute(any());
        }

        private OutputFindAllCustomerProductsDTO getOutputFindAllCustomerProductsDTO(MvcResult result)
            throws JsonProcessingException, UnsupportedEncodingException
        {
            final String responseBody = result.getResponse().getContentAsString();
            return objectMapper.readValue(responseBody, OutputFindAllCustomerProductsDTO.class);
        }

        private OutputFindAllCustomerProductsDTO getOutputFindAllCustomerProductsDTO() {
            final OutputFindAllCustomerProductsDTO.OutputFindAllCustomerProductCustomerDTO customer =
                new OutputFindAllCustomerProductsDTO.OutputFindAllCustomerProductCustomerDTO(
                    "C1",
                    "Customer 1"
                );
            final OutputFindAllCustomerProductsDTO.OutputFindAllCustomerProductProductDTO product1 =
                new OutputFindAllCustomerProductsDTO.OutputFindAllCustomerProductProductDTO(
                    "P2",
                    "Product 2",
                    89.5
                );
            final OutputFindAllCustomerProductsDTO.OutputFindAllCustomerProductProductDTO product2 =
                new OutputFindAllCustomerProductsDTO.OutputFindAllCustomerProductProductDTO(
                    "P2",
                    "Product 2",
                    89.5
                );
            return new OutputFindAllCustomerProductsDTO(
                customer,
                Arrays.asList(product1, product2)
            );
        }
    }
}
