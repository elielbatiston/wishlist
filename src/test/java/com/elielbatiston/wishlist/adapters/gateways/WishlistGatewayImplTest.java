package com.elielbatiston.wishlist.adapters.gateways;

import com.elielbatiston.wishlist.adapters.gateways.models.WishlistModel;
import com.elielbatiston.wishlist.configs.InternationalizationConfig;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistGatewayImplTest {
    @Mock
    private WishlistRepository repository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MessagesHelper messagesHelper;

    @InjectMocks
    private WishlistGatewayImpl gateway;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InternationalizationConfig config;

    @Nested
    class Create {
        @Test
        public void testSave() {
            final Wishlist wishlist = getWishlist(null);
            gateway.save(wishlist);
            verify(repository).save(any());
        }
    }

    @Nested
    class Get {
        @Test
        public void testGetWishlist() {
            String id = "123456789012345678901234";
            final Wishlist wishlist = getWishlist(id);
            when(repository.findByIdCustomer(any())).thenReturn(Optional.of(WishlistModel.fromDomain(wishlist)));
            final Wishlist actual = gateway.getWishlist(id);
            verify(repository).findByIdCustomer(any());

            assertEquals(wishlist.getCustomer().getId(), actual.getCustomer().getId());
            assertEquals(wishlist.getCustomer().getName(), actual.getCustomer().getName());
            assertEquals(1, actual.getProducts().size());

            List<Product> expectedList
                = new ArrayList<>(wishlist.getProducts());
            List<Product> actualList
                = new ArrayList<>(actual.getProducts());

            assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
            assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
            assertEquals(expectedList.get(0).getPrice(), actualList.get(0).getPrice());
        }

        @Test
        public void testGetWishlistShouldThrowObjectException() {
            String id = "123456789012345678901234";
            final String message = String.format("Objeto não encontrado! Id: %s, Tipo: %s", id, Wishlist.class);
            when(repository.findByIdCustomer(any())).thenReturn(Optional.empty());
            lenient().when(config.getInternationalizedMessage(any())).thenReturn(message);
            lenient().when(messagesHelper.getExceptionMessageObjectNotFound(any(), any())).thenReturn(message);
            ObjectNotFoundException exception =
                assertThrowsExactly(ObjectNotFoundException.class, () -> gateway.getWishlist(id));
            verify(repository).findByIdCustomer(any());
            assertEquals(ObjectNotFoundException.class, exception.getClass());
            assertEquals(message, exception.getMessage());
        }
    }

    @Nested
    class Delete {
        @Test
        public void testDelete() {
            String id = "123456789012345678901234";
            final Wishlist wishlist = getWishlist(id);
            when(repository.findById(any())).thenReturn(Optional.of(WishlistModel.fromDomain(wishlist)));
            gateway.delete(id);
            verify(repository).findById(any());
            verify(repository).delete(any());
        }

        @Test
        public void testDelete1() {
            String id = "123456789012345678901234";
            when(repository.findById(any())).thenReturn(Optional.empty());
            assertThrowsExactly(ObjectNotFoundException.class, () -> gateway.delete(id));
            verify(repository).findById(any());
            verify(repository, never()).delete(any());
        }
    }

    private Wishlist getWishlist(String id) {
        final Customer customer = new Customer(
            "C1",
            "Nome 1"
        );
        final Product product = new Product(
            "P1",
            "Produto 1",
            99.5
        );
        final Wishlist wishlist = new Wishlist(
            id,
            customer
        );
        wishlist.addProduct(product);
        return wishlist;
    }
}
