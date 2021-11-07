package domain.validators;

import domain.Friendship;
import domain.User;

/**
 * friendship validator
 */
public class FriendshipValidator implements Validator<Friendship> {
    /**
     *
     * @param entity -> must not be null
     * @throws ValidationException negative id
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getFriendId1() <= 0)
            throw new ValidationException("negative id for friend 1");
        if(entity.getFriendId2() <= 0)
            throw new ValidationException("negative id for friend 2");
    }
}
