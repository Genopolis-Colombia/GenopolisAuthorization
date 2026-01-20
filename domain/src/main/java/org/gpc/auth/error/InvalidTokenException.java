package org.gpc.auth.error;

public class InvalidTokenException extends Exception {

    public InvalidTokenException(String message) {
        super(message);
    }
}
