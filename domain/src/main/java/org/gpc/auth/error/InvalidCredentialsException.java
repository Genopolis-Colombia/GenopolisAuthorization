package org.gpc.auth.error;

public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(){
        super("The password is not valid");
    }
}
