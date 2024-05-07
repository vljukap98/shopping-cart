package ljakovic.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class ShoppingCart {

    @Id
    private String id;
    private String customerId;
    private String paidAt;
    @DBRef
    private List<ShoppingItem> items;

    public Date getDatePaidAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(paidAt);
        } catch (ParseException e) {
            //
        }
        return null;
    }

    public void setDatePaidAt(Date paidAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.paidAt = sdf.format(paidAt);
    }
}
