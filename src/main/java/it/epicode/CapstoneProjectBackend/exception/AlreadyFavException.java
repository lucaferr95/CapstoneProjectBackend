package it.epicode.CapstoneProjectBackend.exception;

public class AlreadyFavException extends RuntimeException{
    public AlreadyFavException(String message) {
        super(message);
    }
}