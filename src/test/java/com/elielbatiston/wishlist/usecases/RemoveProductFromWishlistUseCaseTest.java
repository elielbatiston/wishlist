package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.dto.InputRemoveProductFromWishlist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveProductFromWishlistUseCaseTest {

    @Mock
    private WishlistGateway gateway;

    @Mock
    private MessagesHelper messagesHelper;

    @Spy
    @InjectMocks
    private RemoveProductFromWishlistUseCase usecase;

    @Test
    public void testExecuteWishlistWithTwoProducts() {
        final InputRemoveProductFromWishlist dto = new InputRemoveProductFromWishlist("C1", "P1");
        final Wishlist wishlist = getWishlist(2);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
        verify(gateway).save(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(1, actual.getProducts().size());
        assertEquals(wishlist.getCustomer().getId(), actual.getCustomer().getId());
        assertEquals(wishlist.getCustomer().getName(), actual.getCustomer().getName());
        assertEquals(wishlist.getProducts().get(0).getId(), actual.getProducts().get(0).getId());
        assertEquals(wishlist.getProducts().get(0).getName(), actual.getProducts().get(0).getName());
        assertEquals(wishlist.getProducts().get(0).getPrice(), actual.getProducts().get(0).getPrice());
    }

    @Test
    public void testExecuteWishlistWithAProduct() {
        final InputRemoveProductFromWishlist dto = new InputRemoveProductFromWishlist("C1", "P1");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        usecase.execute(dto);
        verify(gateway).getWishlist(any());
        verify(gateway).save(any());

        final ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(gateway).save(captor.capture());
        final Wishlist actual = captor.getValue();
        assertEquals(0, actual.getProducts().size());
    }

    @Test
    public void testExecuteWishlistWithoutProductsThenThrowObjectNotFound() {
        final InputRemoveProductFromWishlist dto = new InputRemoveProductFromWishlist("C1", "P1");
        final Wishlist wishlist = getWishlist(0);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        when(messagesHelper.getExceptionMessageObjectNotFound(any(), any())).thenReturn("mock");
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
        verify(gateway, never()).save(any());
    }

    @Test
    public void testExecuteWishlistWithProductsThenThrowObjectNotFound() {
        final InputRemoveProductFromWishlist dto = new InputRemoveProductFromWishlist("C1", "P3");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        when(messagesHelper.getExceptionMessageObjectNotFound(any(), any())).thenReturn("mock");
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
        verify(gateway, never()).save(any());
    }

    @Test
    public void testExecuteWishlistWithACustomerNotFound() {
        final InputRemoveProductFromWishlist dto = new InputRemoveProductFromWishlist("C2", "P1");
        when(gateway.getWishlist(any())).thenThrow(new ObjectNotFoundException("Error"));
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
        verify(gateway, never()).save(any());
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
