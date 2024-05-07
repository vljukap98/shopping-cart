package ljakovic.shoppingcart.service;

import ljakovic.shoppingcart.dto.OfferStatsDto;
import ljakovic.shoppingcart.dto.ShoppingCartDto;
import ljakovic.shoppingcart.dto.ShoppingItemDto;
import ljakovic.shoppingcart.exception.EntityNotFoundException;
import ljakovic.shoppingcart.mapper.ShoppingCartMapper;
import ljakovic.shoppingcart.model.EAction;
import ljakovic.shoppingcart.model.ShoppingCart;
import ljakovic.shoppingcart.model.ShoppingItem;
import ljakovic.shoppingcart.repository.ShoppingCartRepository;
import ljakovic.shoppingcart.repository.ShoppingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShoppingCartService {

    private static final String SHOPPING_CART_NOT_FOUND = "Shopping cart not found for user: ";
    private static final String SHOPPING_ITEM_NOT_FOUND = "Shopping item not found for id: ";

    @Autowired
    private ShoppingCartRepository shoppingCartRepo;
    @Autowired
    private ShoppingItemRepository shoppingItemRepo;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ShoppingCartDto getUserShoppingCart(String customerId) {
        ShoppingCart shoppingCart = shoppingCartRepo.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND + customerId));

        return shoppingCartMapper.mapTo(shoppingCart);
    }

    public ShoppingCartDto createShoppingCart(String customerId) {
        ShoppingCart shoppingCart = shoppingCartRepo.findByCustomerId(customerId)
                .orElse(null);

        if (shoppingCart == null) {
            shoppingCart = ShoppingCart.builder()
                    .customerId(customerId)
                    .items(new ArrayList<>())
                    .build();
        }

        shoppingCartRepo.save(shoppingCart);

        return shoppingCartMapper.mapTo(shoppingCart);

    }

    public ShoppingCartDto addShoppingItem(String customerId, ShoppingItemDto dto) {
        ShoppingCart shoppingCart = shoppingCartRepo.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND + customerId));

        if (shoppingCart.getPaidAt() != null) {
            return shoppingCartMapper.mapTo(shoppingCart);
        }

        ShoppingItem shoppingItem = ShoppingItem.builder()
                .action(EAction.ADD)
                .offerIdentification(dto.getOfferIdentification())
                .price(dto.getPrice())
                .shoppingCart(shoppingCart)
                .build();

        shoppingItemRepo.save(shoppingItem);

        shoppingCart.getItems().add(shoppingItem);

        shoppingCartRepo.save(shoppingCart);

        return shoppingCartMapper.mapTo(shoppingCart);
    }

    public ShoppingItemDto modifyShoppingCartItem(String itemId,
                                                  ShoppingItemDto dto) {
        ShoppingItem cartItem = shoppingItemRepo.findById(itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_ITEM_NOT_FOUND + itemId));

        ShoppingCart shoppingCart = shoppingCartRepo.findById(cartItem.getShoppingCart().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND));

        if (shoppingCart.getPaidAt() != null) {
            return null;
        }

        cartItem.setOfferIdentification(dto.getOfferIdentification());
        cartItem.setPrice(dto.getPrice());
        cartItem.setAction(EAction.MODIFY);
        shoppingItemRepo.save(cartItem);

        return shoppingCartMapper.mapTo(cartItem);

    }

    public ShoppingItemDto deleteShoppingCartItem(String itemId) {
        ShoppingItem cartItem = shoppingItemRepo.findById(itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_ITEM_NOT_FOUND + itemId));

        ShoppingCart shoppingCart = shoppingCartRepo.findById(cartItem.getShoppingCart().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND));

        if (shoppingCart.getPaidAt() != null) {
            return null;
        }

        cartItem.setAction(EAction.DELETE);

        shoppingItemRepo.save(cartItem);

        return shoppingCartMapper.mapTo(cartItem);

    }

    public void removeShoppingCartItem(String itemId) {
        ShoppingItem cartItem = shoppingItemRepo.findById(itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_ITEM_NOT_FOUND + itemId));

        ShoppingCart shoppingCart = shoppingCartRepo.findById(cartItem.getShoppingCart().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND));

        if (shoppingCart.getPaidAt() != null) {
            return;
        }

        shoppingItemRepo.delete(cartItem);
    }

    public void evictCart(String customerId) {
        ShoppingCart shoppingCart = shoppingCartRepo.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND + customerId));

        if (shoppingCart.getPaidAt() != null) {
            return;
        }

        shoppingCart.getItems().forEach(i -> shoppingItemRepo.delete(i));

        shoppingCart.setPaidAt(null);
        shoppingCartRepo.save(shoppingCart);
    }

    public void payCart(String customerId) {
        ShoppingCart shoppingCart = shoppingCartRepo.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(SHOPPING_CART_NOT_FOUND + customerId));

        if (shoppingCart.getPaidAt() != null) {
            return;
        } else {
            shoppingCart.setDatePaidAt(new Date());
        }

        shoppingCartRepo.save(shoppingCart);
    }

    public List<OfferStatsDto> getSoldOffers(String offerId, EAction action, Date startDate, Date endDate) {
        //TODO: tweak body of this method
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("items"),
                Aggregation.match(
                        Criteria.where("paidAt").gte(startDate).lte(endDate)
                                .and("items.offerIdentification").is(offerId)
                                .and("items.action").is(action.toString())
                ),
                Aggregation.group("items.offerIdentification", "items.action")
                        .count().as("quantitySold")
        );

        AggregationResults<OfferStatsDto> aggregationResults = mongoTemplate.aggregate(aggregation, "shoppingCart", OfferStatsDto.class);

        return aggregationResults.getMappedResults();
    }
}
