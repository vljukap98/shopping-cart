package ljakovic.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ljakovic.shoppingcart.model.EAction;
import ljakovic.shoppingcart.model.Price;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingItemDto {
    private String id;
    @NotBlank
    private String offerIdentification;
    private EAction action;
    @NotNull
    private Price price;
    private String shoppingCartId;
}
