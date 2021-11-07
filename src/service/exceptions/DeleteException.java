package service.exceptions;

/**
 * delete exception
 */
public class DeleteException extends RuntimeException{
    /**
     *
     */
    public DeleteException() {
    }

    /**
     *
     * @param message .
     */
    public DeleteException(String message) {
        super(message);
    }

    /**
     *
     * @param message .
     * @param cause .
     */
    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause .
     */
    public DeleteException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message .
     * @param cause .
     * @param enableSuppression .
     * @param writableStackTrace .
     */
    public DeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
