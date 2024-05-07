package ljakovic.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart_items")
public class ShoppingItem {

    @Id
    private String id;
    private String offerIdentification;
    private EAction action;
    private Price price;
    @DBRef
    private ShoppingCart shoppingCart;
}
