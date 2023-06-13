package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.JsonUtil;
import com.elielbatiston.wishlist.adapters.controllers.exceptions.ControllerExceptionHandler;
import com.elielbatiston.wishlist.adapters.controllers.exceptions.ValidationError;
import com.elielbatiston.wishlist.configs.untestable.ApplicationConfig;
import com.elielbatiston.wishlist.configs.untestable.InterceptorHandle;
import com.elielbatiston.wishlist.configs.InternationalizationConfig;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.add.AddProductUseCase;
import com.elielbatiston.wishlist.usecases.find.FindProductUseCase;
import com.elielbatiston.wishlist.usecases.find.InputFindProductDTO;
import com.elielbatiston.wishlist.usecases.find.OutputFindProductDTO;
import com.elielbatiston.wishlist.usecases.findall.FindAllProductsUseCase;
import com.elielbatiston.wishlist.usecases.delete.InputDeleteProductDTO;
import com.elielbatiston.wishlist.usecases.delete.DeleteProductUseCase;
import com.elielbatiston.wishlist.usecases.add.InputAddProductDTO;
import com.elielbatiston.wishlist.usecases.findall.InputFindAllProductsDTO;
import com.elielbatiston.wishlist.usecases.findall.OutputFindAllProductsDTO;
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
import java.util.*;

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
    private AddProductUseCase addProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockBean
    private FindAllProductsUseCase findAllProductsUseCase;

    @MockBean
    private FindProductUseCase findProductUseCase;

    @Autowired
    private InternationalizationConfig config;

    @Nested
    class Create {
        @Test
        public void testCreate() throws Exception {
            final InputAddProductDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
            mockMvc.perform(
                MockMvcRequestBuilders.post("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
                    .content(objectMapper.writeValueAsString(dto))
            ).andExpect(MockMvcResultMatchers.status().isCreated());
            assertDoesNotThrow(() -> verify(addProductUseCase).execute(any()));
        }

        @Test
        public void testCreateWithoutPayload() throws Exception {
            mockMvc.perform(
                MockMvcRequestBuilders.post("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
                    .content(objectMapper.writeValueAsString(null))
            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
            verify(addProductUseCase, never()).execute(any());
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
            verify(addProductUseCase, never()).execute(any());

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

        private InputAddProductDTO getDTO(String path) {
            final JsonUtil<InputAddProductDTO> jsonUtil = new JsonUtil<>(InputAddProductDTO.class);
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
            assertDoesNotThrow(() -> verify(deleteProductUseCase).execute(any()));
        }

        @Test
        public void testDeleteShouldThrowObjectNotFoundException() throws Exception {
            doThrow(ObjectNotFoundException.class)
                .when(deleteProductUseCase)
                .execute(any(InputDeleteProductDTO.class));
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/wishlist/C1/product/P1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound());
            verify(deleteProductUseCase).execute(any());
        }
    }

    @Nested
    class GetWishlist {
        @Test
        public void testGetWishlist() throws Exception {
            final OutputFindAllProductsDTO output = getOutputFindAllCustomerProductsDTO();
            when(findAllProductsUseCase.execute(any())).thenReturn(output);
            final MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/wishlist/C1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
            )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
            assertDoesNotThrow(() -> verify(findAllProductsUseCase).execute(any()));
            final OutputFindAllProductsDTO actual = getOutputFindAllCustomerProductsDTO(result);
            assertInstanceOf(OutputFindAllProductsDTO.class, actual);
            assertEquals(output.customer().id(), actual.customer().id());
            assertEquals(output.customer().name(), actual.customer().name());
            assertEquals(2, actual.products().size());

            List<OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO> expectedList
                = new ArrayList<>(output.products());
            List<OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO> actualList
                = new ArrayList<>(actual.products());

            assertEquals(expectedList.get(0).id(), actualList.get(0).id());
            assertEquals(expectedList.get(0).name(), actualList.get(0).name());
            assertEquals(expectedList.get(0).price(), actualList.get(0).price());
            assertEquals(expectedList.get(1).id(), actualList.get(1).id());
            assertEquals(expectedList.get(1).name(), actualList.get(1).name());
            assertEquals(expectedList.get(1).price(), actualList.get(1).price());
        }

        @Test
        public void testDeleteShouldThrowObjectNotFoundException() throws Exception {
            doThrow(ObjectNotFoundException.class)
                .when(findAllProductsUseCase)
                .execute(any(InputFindAllProductsDTO.class));
            mockMvc.perform(
                MockMvcRequestBuilders.get("/wishlist/C1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound());
            verify(findAllProductsUseCase).execute(any());
        }

        private OutputFindAllProductsDTO getOutputFindAllCustomerProductsDTO(MvcResult result)
            throws JsonProcessingException, UnsupportedEncodingException
        {
            final String responseBody = result.getResponse().getContentAsString();
            return objectMapper.readValue(responseBody, OutputFindAllProductsDTO.class);
        }

        private OutputFindAllProductsDTO getOutputFindAllCustomerProductsDTO() {
            final OutputFindAllProductsDTO.OutputFindAllCustomerProductCustomerDTO customer =
                new OutputFindAllProductsDTO.OutputFindAllCustomerProductCustomerDTO(
                    "C1",
                    "Customer 1"
                );
            final OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO product1 =
                new OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO(
                    "P2",
                    "Product 2",
                    89.5
                );
            final OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO product2 =
                new OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO(
                    "P1",
                    "Product 1",
                    89.5
                );
            return new OutputFindAllProductsDTO(
                customer,
                Set.of(product1, product2)
            );
        }
    }

    @Nested
    class GetAProduct {
        @Test
        public void testGetAProduct() throws Exception {
            final OutputFindProductDTO output = getOutputFindAProductDTO();
            when(findProductUseCase.execute(any())).thenReturn(output);
            final MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/wishlist/C1/P1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt_BR")
            )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
            assertDoesNotThrow(() -> verify(findProductUseCase).execute(any()));
            final OutputFindProductDTO actual = getOutputFindAProductDTO(result);
            assertInstanceOf(OutputFindProductDTO.class, actual);
            assertEquals(output.customer().id(), actual.customer().id());
            assertEquals(output.customer().name(), actual.customer().name());
            assertEquals(output.product().id(), actual.product().id());
            assertEquals(output.product().name(), actual.product().name());
            assertEquals(output.product().price(), actual.product().price());
        }

        @Test
        public void testGetAProductShouldThrowObjectNotFoundException() throws Exception {
            doThrow(ObjectNotFoundException.class)
                    .when(findProductUseCase)
                    .execute(any(InputFindProductDTO.class));
            mockMvc.perform(
                MockMvcRequestBuilders.get("/wishlist/C1/P1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound());

            verify(findProductUseCase).execute(any());
        }

        private OutputFindProductDTO getOutputFindAProductDTO(MvcResult result)
            throws JsonProcessingException, UnsupportedEncodingException
        {
            final String responseBody = result.getResponse().getContentAsString();
            return objectMapper.readValue(responseBody, OutputFindProductDTO.class);
        }

        private OutputFindProductDTO getOutputFindAProductDTO() {
            final OutputFindProductDTO.OutputFindAProductCustomerDTO customer =
                new OutputFindProductDTO.OutputFindAProductCustomerDTO(
                    "C1",
                    "Customer 1"
                );
            final OutputFindProductDTO.OutputFindAProductProductDTO product =
                new OutputFindProductDTO.OutputFindAProductProductDTO(
                    "P1",
                    "Product 1",
                    89.5
                );
            return new OutputFindProductDTO(
                customer,
                product
            );
        }
    }
}
