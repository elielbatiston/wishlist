package com.elielbatiston.wishlist.adapters.gateways.repositories;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.adapters.gateways.repositories.models.WishlistModel;
import com.elielbatiston.wishlist.config.InternationalizationConfig;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistGatewayImplTest {
    @Mock
    private WishlistRepository repository;

    @Mock
    private MessagesHelper messagesHelper;

    @InjectMocks
    private WishlistGatewayImpl gateway;

    @Mock(lenient = true, answer = Answers.RETURNS_DEEP_STUBS)
    private InternationalizationConfig config;

    @Test
    public void testSave() {
        final Wishlist wishlist = getWishlist(null);
        gateway.save(wishlist);
        verify(repository).save(any());
    }

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
        assertEquals(wishlist.getProducts().get(0).getId(), actual.getProducts().get(0).getId());
        assertEquals(wishlist.getProducts().get(0).getName(), actual.getProducts().get(0).getName());
        assertEquals(wishlist.getProducts().get(0).getPrice(), actual.getProducts().get(0).getPrice());
    }

    @Test
    public void testGetWishlistShouldThrowObjectException() {
        String id = "123456789012345678901234";
        final String message = String.format("Objeto não encontrado! Id: %s, Tipo: %s", id, Wishlist.class);
        when(repository.findByIdCustomer(any())).thenReturn(Optional.empty());
        when(config.getInternationalizedMessage(any())).thenReturn(message);
        when(messagesHelper.getExceptionMessageObjectNotFound(any(), any())).thenReturn(message);
        ObjectNotFoundException exception = assertThrowsExactly(ObjectNotFoundException.class, () -> {
            gateway.getWishlist(id);
        });
        verify(repository).findByIdCustomer(any());
        assertEquals(ObjectNotFoundException.class, exception.getClass());
        assertEquals(message, exception.getMessage());
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
