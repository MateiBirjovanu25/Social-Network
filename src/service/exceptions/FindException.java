package service.exceptions;

public class FindException extends RuntimeException{
    public FindException() {
    }

    public FindException(String message) {
        super(message);
    }

    public FindException(String message, Throwable cause) {
        super(message, cause);
    }

    public FindException(Throwable cause) {
        super(cause);
    }

    public FindException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
