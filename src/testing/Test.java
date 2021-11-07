package testing;

import domain.User;
import domain.validators.UserValidator;
import repository.database.UserDatabase;

public class Test {
    public void userDatabaseTest(){
        UserDatabase repoDb = new UserDatabase(new UserValidator(),"jdbc:postgresql://localhost:5432/social_network","postgres","mat25022001");
        User u1 = new User("a1","b1");
        User u2 = new User("a2","b3");
        User u3 = new User("a3","b3");
        repoDb.save(u1);
        repoDb.save(u2);
        repoDb.save(u3);

        User foundUser = repoDb.findOne(1L);
        assert ("a1".equals(foundUser.getFirstName()));

        foundUser = repoDb.findOne(1L);
        assert ("a1".equals(foundUser.getFirstName()));
    }
}
