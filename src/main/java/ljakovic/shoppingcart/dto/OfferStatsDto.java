package ljakovic.shoppingcart.dto;

import ljakovic.shoppingcart.model.EAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferStatsDto {
    private String offerIdentification;
    private EAction action;
    private long quantitySold;
}
