package com.example.map211psvm.services;

import com.example.map211psvm.domain.Community;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunityService {
    private Repository<Long, User> userRepository;

    /** Class constructor.
     *
     * @param userRepository - the repository of users.
     */
    public CommunityService(Repository<Long, User> userRepository) {
        this.userRepository = userRepository;
    }

    /** Makes the adjacency list.
     *
     * @param adjList - a HashMap witch is going to be the adjacency list.
     */
    private void makeAdjList(HashMap<User, List<User>> adjList){
        userRepository.findAll().forEach(user -> adjList.put(user, user.getFriendList()));
    }

    /** Finds a community starting from a given user.
     *
     * @param user - the user from whom the DFS search begins.
     * @param visited - a boolean HashMap where are the visited users.
     * @param community - the list that stores the community.
     * @param adjList - the adjacency list.
     */
    private void findCommunity(User user, HashMap<User, Boolean> visited, ArrayList<User> community, HashMap<User, List<User>> adjList){
        visited.put(user, true);
        community.add(user);
        var friendList = adjList.get(user);
        friendList.forEach(friend -> {
            if(!visited.containsKey(friend))
                findCommunity(friend, visited, community, adjList);
        });
    }

    /** Finds all the communities.
     *
     * @return an ArrayList containing all the communities.
     */
    private ArrayList<Community> findAllCommunities(){
        HashMap<User, List<User>> adjList = new HashMap<>();
        makeAdjList(adjList);
        HashMap<User, Boolean> visited = new HashMap<>();
        ArrayList<Community> communities = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            ArrayList<User> al = new ArrayList<>();
            if(!visited.containsKey(user)) {
                findCommunity(user, visited, al, adjList);
                communities.add(new Community(al));
            }
        });
        return communities;
    }

    /** Gets the number of communities.
     *
     * @return the number of communities.
     */
    public int getNumberOfCommunities(){
        ArrayList<Community> communities = findAllCommunities();
        return communities.size();
    }

    /** Gets the most sociable community.
     *
     * @return the most sociable community.
     */
    public List<User> getMostSociableCommunity(){
        ArrayList<Community> communities = findAllCommunities();
        Community mostSociableCommunity = communities.get(0);
        int maxim = communities.get(0).getUserList().size();
        for(var community : communities)
            if(maxim < community.getUserList().size()) {
                maxim = community.getUserList().size();
                mostSociableCommunity = community;
            }
        return mostSociableCommunity.getUserList();
    }
}
