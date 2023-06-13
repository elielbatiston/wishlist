package com.elielbatiston.wishlist.adapters.controllers;

import com.elielbatiston.wishlist.usecases.add.AddProductUseCase;
import com.elielbatiston.wishlist.usecases.find.FindProductUseCase;
import com.elielbatiston.wishlist.usecases.find.InputFindProductDTO;
import com.elielbatiston.wishlist.usecases.find.OutputFindProductDTO;
import com.elielbatiston.wishlist.usecases.findall.FindAllProductsUseCase;
import com.elielbatiston.wishlist.usecases.delete.InputDeleteProductDTO;
import com.elielbatiston.wishlist.usecases.delete.DeleteProductUseCase;
import com.elielbatiston.wishlist.usecases.add.InputAddProductDTO;
import com.elielbatiston.wishlist.usecases.findall.InputFindAllProductsDTO;
import com.elielbatiston.wishlist.usecases.findall.OutputFindAllProductsDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value=WishlistController.WISHLIST_RESOURCE)
public class WishlistController {

    public static final String WISHLIST_RESOURCE = "/wishlist";
    private static final String WISHLIST_DELETE_A_PRODUCT_ENDPOINT = "/{idCustomer}/product/{idProduct}";
    private static final String WISHLIST_GET_WISHLIST_ENDPOINT = "/{idCustomer}";
    private static final String WISHLIST_GET_A_PRODUCT_IN_WISHLIST_ENDPOINT = "/{idCustomer}/{idProduct}";

    @Autowired
    private AddProductUseCase addProductUseCase;

    @Autowired
    private DeleteProductUseCase deleteProductUseCase;

    @Autowired
    private FindAllProductsUseCase findAllProductsUseCase;

    @Autowired
    private FindProductUseCase findProductUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void wishlist(@Valid @RequestBody final InputAddProductDTO dto) {
        addProductUseCase.execute(dto);
    }

    @DeleteMapping(WISHLIST_DELETE_A_PRODUCT_ENDPOINT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAProduct(
        @PathVariable final String idCustomer,
        @PathVariable final String idProduct
    ) {
        InputDeleteProductDTO input = new InputDeleteProductDTO(
            idCustomer,
            idProduct
        );
        deleteProductUseCase.execute(input);
    }

    @GetMapping(WISHLIST_GET_WISHLIST_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OutputFindAllProductsDTO> getWishlist(@PathVariable final String idCustomer) {
        final InputFindAllProductsDTO input = new InputFindAllProductsDTO(idCustomer);
        final OutputFindAllProductsDTO output = findAllProductsUseCase.execute(input);
        return ResponseEntity.ok().body(output);
    }

    @GetMapping(WISHLIST_GET_A_PRODUCT_IN_WISHLIST_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OutputFindProductDTO> getAProduct(
        @PathVariable final String idCustomer,
        @PathVariable final String idProduct
    ) {
        final InputFindProductDTO input = new InputFindProductDTO(idCustomer, idProduct);
        final OutputFindProductDTO output = findProductUseCase.execute(input);
        return ResponseEntity.ok().body(output);
    }
}
