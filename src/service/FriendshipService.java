package service;

import domain.Friendship;
import domain.User;
import domain.Tuple;
import service.exceptions.FindException;
import service.exceptions.UpdateException;
import service.graph.Graph;
import repository.Repository;
import service.exceptions.AddException;
import service.exceptions.DeleteException;

import java.util.ArrayList;
import java.util.List;

/**
 * friendship service
 */
public class FriendshipService {

    /**
    * User repository
     */
    private Repository<Long, User> repoUser;
    /**
     * Friendship repository
     */
    private Repository<Tuple<Long,Long>, Friendship> repoFriendship;

    /**
     *
     * @param repoUser -> the user repository
     * @param repoFriendship -> the friendship repository
     * constructor
     */
    public FriendshipService(Repository<Long, User> repoUser, Repository<Tuple<Long,Long>, Friendship> repoFriendship) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;

        reloadFriends();

    }

    /**
    * update the friend lists for all the users
     */
    private void reloadFriends()
    {
        for(User user : repoUser.findAll())
        {
            List<User> friends = new ArrayList<>();
            for(Friendship friendship : repoFriendship.findAll())
            {
                if(user.getId() == friendship.getFriendId1())
                    friends.add(repoUser.findOne(friendship.getFriendId2()));
                else if(user.getId() == friendship.getFriendId2())
                    friends.add(repoUser.findOne(friendship.getFriendId1()));
            }
            user.setFriends(friends);
        }
    }

    /**
    * @param friendId1 first user id, must not be null
    * @param friendId2 second user id, must not be null
     * @exception AddException -> thrown if the friendship already exists or if the specified users do not exist
     */
    public void addFriendship(Long friendId1,Long friendId2){
        Iterable<User> users = repoUser.findAll();
        boolean foundFriend1 = false;
        boolean foundFriend2 = false;
        for(User user : users)
        {
            if(user.getId() == friendId1)
                foundFriend1 = true;
            else if (user.getId() == friendId2)
                foundFriend2 = true;
        }

        if(foundFriend1 && foundFriend2){
            Friendship friendship = new Friendship(friendId1,friendId2);

            Friendship output;
            output = repoFriendship.save(friendship);
            if(output != null)
            {
                throw new AddException("the friendship already exists");
            }
            else
            {
                reloadFriends();
            }
        }
        else{
            throw new AddException("the specified users don't exist");
        }
    }


    /**
    * @param id -> the id of the user, must not be null
     * @exception  DeleteException -> thrown if the friendship does not exist
     */
    public void removeFriendship(Tuple<Long,Long>  id) {
        Friendship friendship = repoFriendship.delete(id);
        if (friendship == null) {
            throw new DeleteException("friendship does not exist");
        }
        reloadFriends();
    }

    public Friendship findFriendship(Tuple<Long,Long> id){
        Friendship friendship = repoFriendship.findOne(id);
        if(friendship == null)
            throw new FindException("Friendship does not exist");
        return friendship;
    }

    /**
    * @return -> the list of friendships
     */
    public Iterable<Friendship> getFriendships()
    {
        return repoFriendship.findAll();
    }

    /**
     * @return -> the number of communities
     */
    public int numberOfCommunities()
    {
        reloadFriends();
        Graph graph = new Graph(repoUser.findAll(),repoFriendship.findAll());
        return graph.numConnectedComponents();
    }

    /**
     * @return -> the list containing the most sociable community
     */
    public ArrayList<Integer> mostSociable()
    {
        reloadFriends();
        Graph graph = new Graph(repoUser.findAll(),repoFriendship.findAll());
        ArrayList<ArrayList<Integer>> communities = graph.connectedComponents();
        ArrayList<Integer> mostSociableCommunity = new ArrayList<>();
        double sociabillityCoefficient = 0;
        for(ArrayList<Integer> community : communities)
        {
            double friendsSum = 0;
            double numMembers = 0;
            for(Integer i : community)
            {
                User user = repoUser.findOne(i.longValue());
                numMembers++;
                friendsSum += user.getFriends().size();
            }
            double coefficient = friendsSum /numMembers;
            if(coefficient > sociabillityCoefficient)
            {
                mostSociableCommunity = community;
                sociabillityCoefficient = coefficient;
            }
            else if(coefficient == sociabillityCoefficient && mostSociableCommunity.size()  < community.size())
            {
                mostSociableCommunity = community;
                sociabillityCoefficient = coefficient;
            }
        }
        return mostSociableCommunity;
    }
}
