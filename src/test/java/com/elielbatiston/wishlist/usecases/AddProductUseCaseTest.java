package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.JsonUtil;
import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.add.AddProductUseCase;
import com.elielbatiston.wishlist.usecases.add.InputAddProductDTO;
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
class AddProductUseCaseTest {

    @Mock
    private WishlistGatewayImpl gateway;

    @Spy
    @InjectMocks
    private AddProductUseCase usecase;

    @Test
    public void testExecuteWishlistIsEmpty() {
        final InputAddProductDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
        when(gateway.getWishlist(any())).thenThrow(new ObjectNotFoundException("Error"));
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
        verify(gateway).save(any());
    }

    @Test
    public void testExecuteWishlistWithAProduct() {
        final InputAddProductDTO dto = getDTO("mock/input_add_product_to_wishlist.json");
        final Wishlist wishlist = getWishlist();
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
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
        final InputAddProductDTO dto = getDTO("mock/input_add_product_to_wishlist_and_change_customer_name.json");
        final Wishlist wishlist = getWishlist();
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
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

    private InputAddProductDTO getDTO(String path) {
        final JsonUtil<InputAddProductDTO> jsonUtil = new JsonUtil<>(InputAddProductDTO.class);
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