package domain.validators;

/**
 * validation exception
 */
public class ValidationException extends RuntimeException {
    /**
     *
     */
    public ValidationException() {
    }

    /**
     *
     * @param message .
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     *
     * @param message .
     * @param cause .
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause .
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message .
     * @param cause .
     * @param enableSuppression .
     * @param writableStackTrace .
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
