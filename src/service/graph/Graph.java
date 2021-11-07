package service.graph;

import domain.Friendship;
import domain.User;

import java.util.ArrayList;

/**
 * service.graph class
 */
public class Graph {
    /**
     * list of users
     */
    Iterable<User> users;
    /**
     * list of friendships
     */
    Iterable<Friendship> friendships;
    /**
     * number of vertexes
     */
    int numV;
    /**
     * service.graph matrix
     */
    int[][] matrix;

    /**
     *
     * @param users -> user list, must not be null
     * @param friendships -> friendship list, must not be null
     */
    public Graph(Iterable<User> users, Iterable<Friendship> friendships) {
        this.users = users;
        this.friendships = friendships;
        initMatrix();
    }

    /**
     * initialize the service.graph
     */
    private void initMatrix()
    {
        for(User user : users)
            numV = user.getId().intValue();
        numV++;

        matrix = new int[numV][numV];
        for(Friendship friendship : friendships)
        {
            int i = Integer.parseInt(Long.toString(friendship.getFriendId1()));
            int j = Integer.parseInt(Long.toString(friendship.getFriendId2()));
            matrix[i][j] = 1;
            matrix[j][i] = 1;
        }
        matrix[0][0] = numV;
    }

    /**
     *
     * @param v -> vertex, not null
     * @param visited -> not null
     * @param numV -> num of vertexes, not null
     * @param matrix -> service.graph matrix, not null
     * @param list -> component list
     */
    private void dfsUtil(int v, boolean[] visited, int numV, int[][] matrix,ArrayList<Integer> list)
    {
        visited[v] = true;
        list.add(v);
        for(int i = 1;i < numV;i++)
        {
            if(!visited[i] && matrix[i][v] == 1)
                dfsUtil(i,visited,numV,matrix,list);
        }
    }

    /**
     *
     * @param id not null
     * @return true if the vertex exists,
     *          false otherwise
     */
    private boolean verifyUser(int id)
    {
        for(User user : users)
        {
            if(user.getId() == id)
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return -> the number of components
     */
    public int numConnectedComponents()
    {
        boolean[] visited =  new boolean[numV];
        int numComponents=0;
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=1;i<numV;i++)
        {
            if(!visited[i] && verifyUser(i))
            {
                dfsUtil(i,visited,numV,matrix,list);
                numComponents++;
            }
        }
        return numComponents;
    }

    /**
     *
     * @return -> the list of connected components
     */
    public ArrayList<ArrayList<Integer>> connectedComponents()
    {
        boolean[] visited = new boolean[numV];
        ArrayList<ArrayList<Integer>> communities = new ArrayList<>();
        for(int i=1;i<numV;i++)
        {
            if(!visited[i] && verifyUser(i))
            {
                ArrayList<Integer> communityList = new ArrayList<>();
                dfsUtil(i,visited,numV,matrix,communityList);
                communities.add(communityList);
            }
        }
        return communities;
    }
}
