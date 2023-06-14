package com.elielbatiston.wishlist.usecases.find;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductUseCaseTest {

    @Mock
    private WishlistGatewayImpl gateway;

    @Mock
    private MessagesHelper messagesHelper;

    @InjectMocks
    private FindProductUseCase usecase;

    @Test
    public void testExecuteWishlistWithTwoProducts() {
        final InputFindProductDTO dto = new InputFindProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(2);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        final OutputFindProductDTO actual = usecase.execute(dto);
        verify(gateway).getWishlist(any());
        assertEquals(wishlist.getCustomer().getId(), actual.customer().id());
        assertEquals(wishlist.getCustomer().getName(), actual.customer().name());

        List<Product> expectedList
                = new ArrayList<>(wishlist.getProducts());

        assertEquals(expectedList.get(0).getId(), actual.product().id());
        assertEquals(expectedList.get(0).getName(), actual.product().name());
        assertEquals(expectedList.get(0).getPrice(), actual.product().price());
    }

    @Test
    public void testExecuteWishlistWithAProduct() {
        final InputFindProductDTO dto = new InputFindProductDTO("C1", "P1");
        final Wishlist wishlist = getWishlist(1);
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        final OutputFindProductDTO actual = usecase.execute(dto);
        verify(gateway).getWishlist(any());
        assertEquals(wishlist.getCustomer().getId(), actual.customer().id());
        assertEquals(wishlist.getCustomer().getName(), actual.customer().name());

        List<Product> expectedList = new ArrayList<>(wishlist.getProducts());

        assertEquals(expectedList.get(0).getId(), actual.product().id());
        assertEquals(expectedList.get(0).getName(), actual.product().name());
        assertEquals(expectedList.get(0).getPrice(), actual.product().price());
    }

    @Test
    public void testExecuteWishlistWithACustomerNotFound() {
        final InputFindProductDTO dto = new InputFindProductDTO("C1", "P1");
        when(gateway.getWishlist(any())).thenThrow(new ObjectNotFoundException("Error"));
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
    }

    @Test
    public void testExecuteWishlistWithAProductNotFound() {
        final InputFindProductDTO dto = new InputFindProductDTO("C1", "P2");
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
