package com.elielbatiston.wishlist.usecases;

import com.elielbatiston.wishlist.adapters.gateways.repositories.WishlistGateway;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import com.elielbatiston.wishlist.usecases.dto.InputFindAllCustomerProductsDTO;
import com.elielbatiston.wishlist.usecases.dto.OutputFindAllCustomerProductsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCustomerProductsUseCaseTest {

    @Mock
    private WishlistGateway gateway;

    @InjectMocks
    private FindAllCustomerProductsUseCase usecase;

    @Test
    public void testExecute() {
        final InputFindAllCustomerProductsDTO dto = new InputFindAllCustomerProductsDTO("C1");
        final Wishlist wishlist = getWishlist();
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        final OutputFindAllCustomerProductsDTO actual = usecase.execute(dto);
        verify(gateway).getWishlist(any());
        assertInstanceOf(OutputFindAllCustomerProductsDTO.class, actual);
        assertEquals(wishlist.getCustomer().getId(), actual.customer().id());
        assertEquals(wishlist.getCustomer().getName(), actual.customer().name());
        assertEquals(2, actual.products().size());
        assertEquals(wishlist.getProducts().get(0).getId(), actual.products().get(0).id());
        assertEquals(wishlist.getProducts().get(0).getName(), actual.products().get(0).name());
        assertEquals(wishlist.getProducts().get(0).getPrice(), actual.products().get(0).price());
        assertEquals(wishlist.getProducts().get(1).getId(), actual.products().get(1).id());
        assertEquals(wishlist.getProducts().get(1).getName(), actual.products().get(1).name());
        assertEquals(wishlist.getProducts().get(1).getPrice(), actual.products().get(1).price());
    }

    @Test
    public void testExecuteShouldThrowObjectNotFoundException() {
        final InputFindAllCustomerProductsDTO dto = new InputFindAllCustomerProductsDTO("C1");
        when(gateway.getWishlist(any())).thenThrow(new ObjectNotFoundException("Error"));
        assertThrowsExactly(ObjectNotFoundException.class, () -> usecase.execute(dto));
        verify(gateway).getWishlist(any());
    }

    private Wishlist getWishlist() {
        final Customer customer = new Customer(
            "C1",
            "Customer 1"
        );
        final Product product1 = new Product(
            "P2",
            "Product 2",
            89.5
        );
        final Product product2 = new Product(
            "P2",
            "Product 2",
            89.5
        );
        final Wishlist wishlist = new Wishlist(
            "123456789012345678901234",
            customer
        );
        wishlist.addProduct(product1);
        wishlist.addProduct(product2);
        return wishlist;
    }
}