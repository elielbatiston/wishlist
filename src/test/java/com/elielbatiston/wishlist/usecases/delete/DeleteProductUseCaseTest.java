package com.elielbatiston.wishlist.usecases.delete;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductUseCaseTest {

    @Mock
    private WishlistGatewayImpl gateway;

    @Mock
    private MessagesHelper messagesHelper;

    @Spy
    @InjectMocks
    private DeleteProductUseCase usecase;

    @Test
    public void testExecuteWishlistWithTwoProducts() {
        final InputDeleteProductDTO dto = new InputDeleteProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(2);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
        verify(gateway).save(any());
        verify(gateway, never()).delete(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(1, actual.getProducts().size());
        assertEquals(wishlist.getCustomer().getId(), actual.getCustomer().getId());
        assertEquals(wishlist.getCustomer().getName(), actual.getCustomer().getName());

        List<Product> expectedList = new ArrayList<>(wishlist.getProducts());
        List<Product> actualList = new ArrayList<>(actual.getProducts());

        assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.get(0).getPrice(), actualList.get(0).getPrice());
    }

    @Test
    public void testExecuteWishlistWithAProduct() {
        final InputDeleteProductDTO dto = new InputDeleteProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
        verify(gateway).save(any());
        verify(gateway).delete(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(0, actual.getProducts().size());
    }

    @Test
    public void testExecuteWishlistWithoutProductsThenThrowObjectNotFound() {
        final InputDeleteProductDTO dto = new InputDeleteProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(0);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        when(messagesHelper.getExceptionMessageObjectNotFound(any(), any())).thenReturn("mock");
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
        verify(gateway, never()).save(any());
        verify(gateway, never()).delete(any());
    }

    @Test
    public void testExecuteWishlistWithProductsThenThrowObjectNotFound() {
        final InputDeleteProductDTO dto = new InputDeleteProductDTO("C1", "P3");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        when(messagesHelper.getExceptionMessageObjectNotFound(any(), any())).thenReturn("mock");
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
        verify(gateway, never()).save(any());
        verify(gateway, never()).delete(any());
    }

    @Test
    public void testExecuteWishlistWithACustomerNotFound() {
        final InputDeleteProductDTO dto = new InputDeleteProductDTO("C2", "P1");
        when(gateway.getWishlist(any())).thenThrow(new ObjectNotFoundException("Error"));
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
        verify(gateway, never()).save(any());
        verify(gateway, never()).delete(any());
    }

    private Wishlist getWishlist(int qty) {
        final Customer customer = new Customer(
            "C1",
            "Customer 1"
        );
        final Wishlist wishlist = new Wishlist(
            "123456789012345678901234",
            customer
        );
        final Random random = new Random();
        for (int count = 0; count < qty; count++) {
            final Double price = random.nextDouble();
            final Product product = new Product(
                String.format("P%s", count + 1),
                String.format("Product %s", count + 1),
                price
            );
            wishlist.addProduct(product);
        }
        return wishlist;
    }
}
