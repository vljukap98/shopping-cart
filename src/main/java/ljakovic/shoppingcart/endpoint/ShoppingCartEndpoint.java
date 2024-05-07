package ljakovic.shoppingcart.endpoint;

import jakarta.validation.Valid;
import ljakovic.shoppingcart.dto.ShoppingItemDto;
import ljakovic.shoppingcart.dto.SoldOffersSearchDto;
import ljakovic.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shopping-cart")
public class ShoppingCartEndpoint {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getUserShoppingCart(@PathVariable String customerId) {
        return ResponseEntity.ok(shoppingCartService.getUserShoppingCart(customerId));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<?> createShoppingCart(@PathVariable String customerId) {
        return ResponseEntity.ok(shoppingCartService.createShoppingCart(customerId));
    }

    @PostMapping("/{customerId}/add-item")
    public ResponseEntity<?> addItemToShoppingCart(@PathVariable String customerId,
                                                   @Valid @RequestBody ShoppingItemDto dto) {
        return ResponseEntity.ok(shoppingCartService.addShoppingItem(customerId, dto));
    }

    @PutMapping("/update-item/{itemId}")
    public ResponseEntity<?> modifyShoppingCartItem(@PathVariable String itemId,
                                                    @Valid @RequestBody ShoppingItemDto dto) {
        return ResponseEntity.ok(shoppingCartService.modifyShoppingCartItem(itemId, dto));
    }

    @DeleteMapping("/{itemId}/delete-item")
    public ResponseEntity<?> deleteShoppingCartItem(@PathVariable String itemId) {
        return ResponseEntity.ok(shoppingCartService.deleteShoppingCartItem(itemId));
    }

    @DeleteMapping("/{itemId}/remove-item")
    public ResponseEntity<?> removeShoppingCartItem(@PathVariable String itemId) {
        shoppingCartService.removeShoppingCartItem(itemId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{customerId}/evict-cart")
    public ResponseEntity<?> evictCart(@PathVariable String customerId) {
        shoppingCartService.evictCart(customerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{customerId}/pay-shopping-cart")
    public ResponseEntity<?> payCart(@PathVariable String customerId) {
        shoppingCartService.payCart(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/get-sold-offers")
    public ResponseEntity<?> getSoldOffers(@RequestBody SoldOffersSearchDto dto) {
        return ResponseEntity.ok(
                shoppingCartService.getSoldOffers(
                    dto.getOfferIdentification(),
                    dto.getAction(),
                    dto.getStartDate(),
                    dto.getEndDate())
        );
    }
}
