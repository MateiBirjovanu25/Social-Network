package domain.validators;

import domain.User;

/**
 * user validator
 */
public class UserValidator implements Validator<User> {
    /**
     *
     * @param entity -> must not be null
     * @throws ValidationException null name
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName() == "")
            throw new ValidationException("null first name");
        if(entity.getLastName() == "")
            throw new ValidationException("null last name");
    }
}