package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.JsonUtil;
import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.usecases.dto.InputAddProductToWishlistDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductToWishlistUseCaseTest {

    @Mock
    private WishlistGateway gateway;

    @Spy
    @InjectMocks
    private AddProductToWishlistUseCase usecase;

    @Test
    public void testExecuteWishlistIsEmpty() {
        final InputAddProductToWishlistDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
        when(gateway.getWishlist(dto.customer().id())).thenReturn(null);
        usecase.execute(dto);
        verify(gateway).getWishlist(dto.customer().id());
        verify(gateway).save(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(1, actual.getProducts().size());
        assertEquals(dto.customer().id(), actual.getCustomer().getId());
        assertEquals(dto.customer().name(), actual.getCustomer().getName());
        assertEquals(dto.product().id(), actual.getProducts().get(0).getId());
        assertEquals(dto.product().name(), actual.getProducts().get(0).getName());
        assertEquals(dto.product().price(), actual.getProducts().get(0).getPrice());
    }

    @Test
    public void testExecuteWishlistWithAProduct() {
        final InputAddProductToWishlistDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
        final Wishlist wishlist = getWishlist();
        when(gateway.getWishlist(dto.customer().id())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(dto.customer().id());
        verify(gateway).save(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(2, actual.getProducts().size());
        assertEquals(dto.customer().id(), actual.getCustomer().getId());
        assertEquals(dto.customer().name(), actual.getCustomer().getName());
        assertEquals("P2", actual.getProducts().get(0).getId());
        assertEquals("Product 2", actual.getProducts().get(0).getName());
        assertEquals(89.5, actual.getProducts().get(0).getPrice());
        assertEquals(dto.product().id(), actual.getProducts().get(1).getId());
        assertEquals(dto.product().name(), actual.getProducts().get(1).getName());
        assertEquals(dto.product().price(), actual.getProducts().get(1).getPrice());
    }

    @Test
    public void testExecuteWishlistWithAProductAndChangeCustomerName() {
        final InputAddProductToWishlistDTO dto = getDTO("mock/input_add_product_to_wishlist_and_change_customer_name.json");
        final Wishlist wishlist = getWishlist();
        when(gateway.getWishlist(dto.customer().id())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(dto.customer().id());
        verify(gateway).save(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(2, actual.getProducts().size());
        assertEquals(dto.customer().id(), actual.getCustomer().getId());
        assertEquals("Customer Change Name", actual.getCustomer().getName());
        assertEquals("P2", actual.getProducts().get(0).getId());
        assertEquals("Product 2", actual.getProducts().get(0).getName());
        assertEquals(89.5, actual.getProducts().get(0).getPrice());
        assertEquals(dto.product().id(), actual.getProducts().get(1).getId());
        assertEquals(dto.product().name(), actual.getProducts().get(1).getName());
        assertEquals(dto.product().price(), actual.getProducts().get(1).getPrice());
    }

    private InputAddProductToWishlistDTO getDTO(String path) {
        final JsonUtil<InputAddProductToWishlistDTO> jsonUtil = new JsonUtil<>(InputAddProductToWishlistDTO.class);
        return jsonUtil.read(path);
    }

    private Wishlist getWishlist() {
        final Customer customer = new Customer(
            "C1",
            "Customer 1"
        );
        final Product product = new Product(
            "P2",
            "Product 2",
            89.5
        );

        final Wishlist wishlist = new Wishlist(
            "123456789012345678901234",
            customer
        );
        wishlist.addProduct(product);
        return wishlist;
    }
}