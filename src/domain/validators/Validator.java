package domain.validators;

/**
 *
 * @param <T> -> generics
 */
public interface Validator<T> {
    /**
     *
     * @param entity -> must not be null
     * @throws ValidationException .
     */
    void validate(T entity) throws ValidationException;
}