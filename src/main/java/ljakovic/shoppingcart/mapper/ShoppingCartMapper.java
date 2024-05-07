package ljakovic.shoppingcart.mapper;

import ljakovic.shoppingcart.dto.ShoppingCartDto;
import ljakovic.shoppingcart.dto.ShoppingItemDto;
import ljakovic.shoppingcart.model.EPriceType;
import ljakovic.shoppingcart.model.Price;
import ljakovic.shoppingcart.model.ShoppingCart;
import ljakovic.shoppingcart.model.ShoppingItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDto mapTo(ShoppingCart shoppingCart) {
        ShoppingCartDto dto = ShoppingCartDto.builder()
                .id(shoppingCart.getId())
                .customerId(shoppingCart.getCustomerId())
                .build();

        if (shoppingCart.getPaidAt() != null) {
            dto.setPaidAt(shoppingCart.getDatePaidAt());
        }
        if (shoppingCart.getItems() != null
                && !shoppingCart.getItems().isEmpty()) {
            List<ShoppingItemDto> items = shoppingCart.getItems().stream()
                    .map(this::mapTo)
                    .toList();

            dto.setItems(items);
        }

        return dto;
    }

    public ShoppingItemDto mapTo(ShoppingItem shoppingItem) {
        return ShoppingItemDto.builder()
                .id(shoppingItem.getId())
                .offerIdentification(shoppingItem.getOfferIdentification())
                .action(shoppingItem.getAction())
                .price(shoppingItem.getPrice())
                .build();
    }
}
