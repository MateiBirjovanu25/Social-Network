package ui;

import domain.Friendship;
import domain.User;
import domain.Tuple;
import domain.validators.ValidationException;
import repository.Repository;
import service.FriendshipService;
import service.UserService;
import service.exceptions.AddException;
import service.exceptions.DeleteException;
import service.exceptions.FindException;
import service.exceptions.UpdateException;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * ui
 */
public class Ui {
    private Repository<Long,User> repoUser;
    private Repository<Tuple<Long,Long>, Friendship> repoFriendship;
    private UserService userService;
    private FriendshipService friendshipService;

    /**
     *
     * @param repoUser repository user
     * @param repoFriendship repository user
     */
    public Ui(Repository<Long, User> repoUser, Repository<Tuple<Long,Long>, Friendship> repoFriendship) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
        userService = new UserService(repoUser,repoFriendship);
        friendshipService = new FriendshipService(repoUser,repoFriendship);
    }

    private void printMenu()
    {
        System.out.println(" ------------Menu------------");
        System.out.println("| 1. Add User                 |");
        System.out.println("| 2. Remove User              |");
        System.out.println("| 3. Update User              |");
        System.out.println("| 4. Add Friendship           |");
        System.out.println("| 5. Remove Friendship        |");
        System.out.println("| 6. View Users               |");
        System.out.println("| 7. View Friendships         |");
        System.out.println("| 8. Number of communities    |");
        System.out.println("| 9. Most sociable community  |");
        System.out.println("| 10. Find user               |");
        System.out.println("| 11. Find friendship         |");
        System.out.println("| x. Exit                     |");
        System.out.println(" ----------------------------");
    }

    /**
     * run ui
     */
    public void  run()
    {
        printMenu();
        Scanner input = new Scanner(System.in);
        System.out.flush();
        label:
        while(true)
        {
            String option = input.nextLine();
            switch (option) {
                case "1":
                    System.out.println("First Name: ");
                    String firstName = input.nextLine();

                    System.out.println("Last Name: ");
                    String lastName = input.nextLine();

                    try {
                        userService.addUser(firstName, lastName);
                        System.out.println("success");
                    }
                    catch (AddException | ValidationException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2": {
                    System.out.println("Id: ");
                    long id=0L;
                    try
                    {
                        id = Long.parseLong(input.nextLine());
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("wrong format");
                        continue ;
                    }
                    try {
                        userService.removeUser(id);
                        System.out.println("success");
                    } catch (DeleteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "3": {
                    long id=0L;
                    try
                    {
                        System.out.println("Id: ");
                        id = Long.parseLong(input.nextLine());
                        System.out.println("First name: ");
                        firstName = input.nextLine();
                        System.out.println("Last name: ");
                        lastName = input.nextLine();
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("wrong format");
                        continue ;
                    }

                    try{
                        userService.updateUser(id,firstName,lastName);
                    }
                    catch (UpdateException | ValidationException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "4":
                    long friendId1=0L,friendId2=0L;

                    try
                    {
                        System.out.println("First Id: ");
                        friendId1 = Long.parseLong(input.nextLine());
                        System.out.println("Second Id: ");
                        friendId2 = Long.parseLong(input.nextLine());
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("wrong format");

                        continue ;
                    }
                    try
                    {
                        friendshipService.addFriendship(friendId1, friendId2);
                        System.out.println("success");
                    }
                    catch (AddException | ValidationException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "5": {

                    long id1=0,id2=0;
                    try
                    {
                        System.out.println("first id: ");
                        id1 = Long.parseLong(input.nextLine());
                        System.out.println("second id: ");
                        id2 = Long.parseLong(input.nextLine());
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("wrong format");
                        continue ;
                    }
                    Tuple<Long,Long> id = new Tuple<>(id1,id2);
                    try {
                        friendshipService.removeFriendship(id);
                        System.out.println("success");
                    } catch (DeleteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "6":
                    Iterable<User> users = userService.getUsers();
                    for (User user : users) {
                        System.out.println(user);
                    }
                    break;
                case "7":
                    Iterable<Friendship> friendships = friendshipService.getFriendships();
                    for (Friendship friendship : friendships) {
                        System.out.println(friendship);
                    }
                    break;
                case "8":
                    System.out.println("Number of communities: " + friendshipService.numberOfCommunities());
                    break;
                case "9":
                    ArrayList<Integer> list = friendshipService.mostSociable();
                    for(int e : list)
                    {
                        System.out.print(e + " ");
                    }
                    System.out.println();
                    break ;
                case "10":
                    long id=0L;
                    try{
                        System.out.println("id: ");
                        id = Long.parseLong(input.nextLine());
                    }
                    catch (NumberFormatException e){
                        System.out.println("invalid format");
                    }
                    User user;
                    try{
                        user = userService.findUser(id);
                        System.out.println(user);
                    }
                    catch (FindException e){
                        System.out.println(e.getMessage());
                    }
                    break ;
                case "11":
                    friendId1=0L;
                    friendId2=0L;
                    try{
                        System.out.println("first id: ");
                        friendId1 = Long.parseLong(input.nextLine());
                        System.out.println("second id: ");
                        friendId2 = Long.parseLong(input.nextLine());
                    }
                    catch (NumberFormatException e){
                        System.out.println("invalid format");
                    }
                    Friendship friendship;
                    Tuple<Long,Long> friendhipId = new Tuple<>(friendId1,friendId2);
                    try{
                        friendship = friendshipService.findFriendship(friendhipId);
                        System.out.println(friendship);
                    }
                    catch (FindException e){
                        System.out.println(e.getMessage());
                    }
                    break ;
                case "x":
                    break label;
            }
            printMenu();
        }
    }
}
