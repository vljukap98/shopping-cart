package ljakovic.shoppingcart.repository;

import ljakovic.shoppingcart.model.ShoppingItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShoppingItemRepository extends MongoRepository<ShoppingItem, String> {
}
