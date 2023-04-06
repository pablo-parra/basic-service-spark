package org.example.core.exception;

public class UnableToGetEmployee extends RuntimeException {
    public UnableToGetEmployee(Throwable cause) {
        super(EXCEPTION_MESSAGE, cause);
    }
    private static final String EXCEPTION_MESSAGE = "Unable to get Employee";
}
