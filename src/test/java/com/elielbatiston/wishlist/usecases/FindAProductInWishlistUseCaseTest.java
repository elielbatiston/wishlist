package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.usecases.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAProductInWishlistUseCaseTest {

    @Mock
    private WishlistGateway gateway;

    @Mock
    private MessagesHelper messagesHelper;

    @InjectMocks
    private FindAProductInWishlistUseCase usecase;

    @Test
    public void testExecuteWishlistWithTwoProducts() {
        final InputFindAProductDTO dto = new InputFindAProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(2);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        final OutputFindAProductDTO actual = usecase.execute(dto);
        verify(gateway).getWishlist(any());
        assertEquals(wishlist.getCustomer().getId(), actual.customer().id());
        assertEquals(wishlist.getCustomer().getName(), actual.customer().name());
        assertEquals(wishlist.getProducts().get(0).getId(), actual.product().id());
        assertEquals(wishlist.getProducts().get(0).getName(), actual.product().name());
        assertEquals(wishlist.getProducts().get(0).getPrice(), actual.product().price());
    }

    @Test
    public void testExecuteWishlistWithAProduct() {
        final InputFindAProductDTO dto = new InputFindAProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        final OutputFindAProductDTO actual = usecase.execute(dto);
        verify(gateway).getWishlist(any());
        assertEquals(wishlist.getCustomer().getId(), actual.customer().id());
        assertEquals(wishlist.getCustomer().getName(), actual.customer().name());
        assertEquals(wishlist.getProducts().get(0).getId(), actual.product().id());
        assertEquals(wishlist.getProducts().get(0).getName(), actual.product().name());
        assertEquals(wishlist.getProducts().get(0).getPrice(), actual.product().price());
    }

    @Test
    public void testExecuteWishlistWithACustomerNotFound() {
        final InputFindAProductDTO dto = new InputFindAProductDTO("C1", "P1");
        when(gateway.getWishlist(any())).thenThrow(new ObjectNotFoundException("Error"));
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
    }

    @Test
    public void testExecuteWishlistWithAProductNotFound() {
        final InputFindAProductDTO dto = new InputFindAProductDTO("C1", "P2");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
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
