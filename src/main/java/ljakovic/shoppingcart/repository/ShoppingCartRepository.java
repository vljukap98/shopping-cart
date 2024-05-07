package ljakovic.shoppingcart.repository;

import ljakovic.shoppingcart.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

    Optional<ShoppingCart> findByCustomerId(String userId);
    boolean existsByCustomerId(String userId);
}
