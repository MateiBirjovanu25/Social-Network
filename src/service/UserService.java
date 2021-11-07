package service;

import domain.Friendship;
import domain.User;
import domain.Tuple;
import repository.Repository;
import service.exceptions.AddException;
import service.exceptions.DeleteException;
import service.exceptions.FindException;
import service.exceptions.UpdateException;

import java.util.ArrayList;

/**
 * user service
 */
public class UserService {
    /**
     * user repository
      */
    private Repository<Long, User> repoUser;
    /**
     * friendship repository
     */
    private Repository<Tuple<Long,Long>, Friendship> repoFriendship;
    /*

     /**
     * user id
     */
    private Long currentUserId = 0L;

    /**
     * constructor
     * @param repoUser -> must not be null
     * @param repoFriendship -> must not be null
     */
    public UserService(Repository<Long, User> repoUser, Repository<Tuple<Long,Long>, Friendship> repoFriendship) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;

        Iterable<User> users = repoUser.findAll();
        Iterable<Friendship> friendships = repoFriendship.findAll();

        for(User user : users)
            currentUserId=user.getId();
        currentUserId++;
    }

    /**
     *
     * @param firstsName -> user first name, must not be null
     * @param lastName -> user last name, must not be null
     */
    public void addUser(String firstsName,String lastName){
        User user = new User(firstsName,lastName);
        user.setId(currentUserId);

        User output;
        output = repoUser.save(user);
        if(output != null)
        {
            throw new AddException("user already exists");
        }
        else
        {
            currentUserId++;
        }
    }

    public void updateUser(Long id,String firstName, String lastName){
        User user = new User(firstName,lastName);
        user.setId(id);

        User output = repoUser.update(user);
        if(output != null)
        {
            throw new UpdateException("user does not exist");
        }
    }

    /**
     *
     * @param id -> must not be null
     */
    public void removeUser(Long id) {
        User user = repoUser.delete(id);
        if (user == null) {
            throw new DeleteException("user does not exist");
        }

        Iterable<Friendship> friendships = repoFriendship.findAll();
        ArrayList<Tuple<Long,Long>> deletedIds = new ArrayList<>();
        for(Friendship friendship : friendships)
        {
            if(friendship.getFriendId1() == user.getId() || friendship.getFriendId2() == user.getId())
            {
                deletedIds.add(friendship.getId());
            }
        }
        for(Tuple<Long,Long> deletedId : deletedIds)
        {
            repoFriendship.delete(deletedId);
        }
    }

    public User findUser(Long id){
        User user = repoUser.findOne(id);
        if(user == null)
            throw new FindException("User does not exist");
        return user;
    }

    /**
     *
     * @return -> the list of users
     */
    public Iterable<User> getUsers()
    {
        return repoUser.findAll();
    }

}
