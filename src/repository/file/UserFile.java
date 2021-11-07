package repository.file;

import domain.User;
import domain.validators.Validator;


import java.util.List;

/**
 * user repository
 */
public class UserFile extends AbstractFileRepository<Long, User> {

    /**
     *
     * @param fileName not null
     * @param validator not null
     */
    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    /**
     * @param attributes -> string list containing all the attributes of the user
     * @return user -> User entity created using the attributes above
     */
    @Override
    protected User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1),attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    /**
     *
     * @param entity
     *          must not be null
     * @return string representing the user
     */
    @Override
    protected String createEntityAsString(User entity)
    {
        String line = entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
        return line;
    }


}