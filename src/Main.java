import domain.Friendship;
import domain.User;
import domain.validators.FriendshipValidator;
import domain.Tuple;
import domain.validators.UserValidator;
import repository.Repository;
import repository.database.FriendshipDatabase;
import repository.database.UserDatabase;
import repository.file.FriendshipFile;
import repository.file.UserFile;
import ui.Ui;

/**
 * main
 */
public class Main {

    /**
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        Repository<Long,User> repoDbUser = new UserDatabase(new UserValidator(),"jdbc:postgresql://localhost:5432/social_network","postgres","mat25022001");
        Repository<Tuple<Long,Long>,Friendship> repoDbFriendship = new FriendshipDatabase(new FriendshipValidator(),"jdbc:postgresql://localhost:5432/social_network","postgres","mat25022001");

        Repository<Long,User> repoFileUser = new UserFile("data/user.csv", new UserValidator());
        Repository<Tuple<Long,Long>,Friendship> repoFileFriendship = new FriendshipFile("data/friendship.csv", new FriendshipValidator());
        Ui ui = new Ui(repoDbUser,repoDbFriendship);
        ui.run();
    }
}
