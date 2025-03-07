package dott.subscription.exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {
    private final Exceptions exceptions;

    public BusinessLogicException(Exceptions exceptions) {
        super(exceptions.getMessage());
        this.exceptions = exceptions;
    }
}