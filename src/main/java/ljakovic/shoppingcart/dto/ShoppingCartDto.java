package ljakovic.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartDto {

    private String id;
    private String customerId;
    private List<ShoppingItemDto> items;
    private Date paidAt;
}
