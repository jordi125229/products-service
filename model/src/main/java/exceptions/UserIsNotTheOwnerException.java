package exceptions;

public class UserIsNotTheOwnerException extends RuntimeException {
    public UserIsNotTheOwnerException(String message) {
        super(message);
    }
}
