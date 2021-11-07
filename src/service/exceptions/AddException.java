package service.exceptions;

/**
 * add exception
 */
public class AddException extends RuntimeException{
    /**
     *
     */
    public AddException() {
    }

    /**
     *
     * @param message .
     */
    public AddException(String message) {
        super(message);
    }

    /**
     *
     * @param message .
     * @param cause .
     */
    public AddException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause .
     */
    public AddException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message .
     * @param cause .
     * @param enableSuppression .
     * @param writableStackTrace .
     */
    public AddException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
