package com.elielbatiston.wishlist.usecases.findall;

import com.elielbatiston.wishlist.adapters.gateways.WishlistGatewayImpl;
import com.elielbatiston.wishlist.domains.Customer;
import com.elielbatiston.wishlist.domains.Product;
import com.elielbatiston.wishlist.domains.Wishlist;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllProductsUseCaseTest {

    @Mock
    private WishlistGatewayImpl gateway;

    @InjectMocks
    private FindAllProductsUseCase usecase;

    @Test
    public void testExecute() {
        final InputFindAllProductsDTO dto = new InputFindAllProductsDTO("C1");
        final Wishlist wishlist = getWishlist();
        when(gateway.getWishlist(any())).thenReturn(wishlist);
        final OutputFindAllProductsDTO actual = usecase.execute(dto);
        verify(gateway).getWishlist(any());
        assertInstanceOf(OutputFindAllProductsDTO.class, actual);
        assertEquals(wishlist.getCustomer().getId(), actual.customer().id());
        assertEquals(wishlist.getCustomer().getName(), actual.customer().name());
        assertEquals(2, actual.products().size());

        List<Product> expectedList = new ArrayList<>(wishlist.getProducts());
        List<OutputFindAllProductsDTO.OutputFindAllCustomerProductProductDTO> actualList = new ArrayList<>(actual.products());

        assertEquals(expectedList.get(0).getId(), actualList.get(0).id());
        assertEquals(expectedList.get(0).getName(), actualList.get(0).name());
        assertEquals(expectedList.get(0).getPrice(), actualList.get(0).price());
        assertEquals(expectedList.get(1).getId(), actualList.get(1).id());
        assertEquals(expectedList.get(1).getName(), actualList.get(1).name());
        assertEquals(expectedList.get(1).getPrice(), actualList.get(1).price());
    }

    @Test
    public void testExecuteShouldThrowObjectNotFoundException() {
        final InputFindAllProductsDTO dto = new InputFindAllProductsDTO("C1");
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
            "P1",
            "Product 1",
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