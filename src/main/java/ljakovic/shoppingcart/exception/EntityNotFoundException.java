package ljakovic.shoppingcart.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String s) {
        super(s);
    }
}
