package repository.file;

import domain.Friendship;
import domain.Tuple;
import domain.validators.Validator;

import java.util.List;

/**
 * friendship repository
 */
public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>,Friendship>{

    /**
     *
     * @param fileName not null
     * @param validator not null
     */
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    /**
     * @param attributes -> string list containing all the attributes of the friendship
     * @return friendship -> Friendship entity created using the attributes above
     */
    @Override
    protected Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)));
        friendship.setId(new Tuple(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1))));
        return friendship;
    }

    /**
     *
     * @param entity
     *          must not be null
     * @return string representing the friendship
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getFriendId1() + ";" + entity.getFriendId2();
    }
}
