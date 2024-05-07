package ljakovic.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ljakovic.shoppingcart.model.EAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoldOffersSearchDto {
    private String offerIdentification;
    private EAction action;
    private Date startDate;
    private Date endDate;

}
