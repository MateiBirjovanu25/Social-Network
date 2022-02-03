package com.example.map211psvm.repository;

import com.example.map211psvm.domain.Friendship;
import com.example.map211psvm.domain.Tuple;
import com.example.map211psvm.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class FriendshipRepository implements RepositoryFriendship{
    private String url;
    private String username;
    private String password;

    public FriendshipRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private User findUser (Long id) throws SQLException {
        String sql = String.format("select * from Users where id_user = %d", id);
        Connection connection = DriverManager.getConnection(url, username, password);
        var resultSet = connection.createStatement().executeQuery(sql);
        connection.close();
        if (resultSet.next())
            return extractUser(resultSet);
        return null;
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        var userId = resultSet.getLong("id_user");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var email = resultSet.getString("email");
        var password = resultSet.getString("password");
        var publicKey = resultSet.getString("public_key");
        var photoPath = resultSet.getString("picture_path");
        return new User(userId, firstName, lastName, email, password, publicKey, photoPath);
    }

    private User extractUserForDisplay(ResultSet resultSet) throws SQLException {
        var userId = resultSet.getLong("id_user");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var email = resultSet.getString("email");
        var photoPath = resultSet.getString("picture_path");
        return new User(userId, firstName, lastName, email, photoPath);
    }

    private Friendship extractFriendship(ResultSet resultSet) throws SQLException {
        var date= resultSet.getDate("date").toLocalDate();
        var firstId = resultSet.getLong("id_user1");
        var secondId = resultSet.getLong("id_user2");
        var firstUser = findUser(firstId);
        var secondUser = findUser(secondId);
        var status = resultSet.getString("status");
        return new Friendship(firstUser, secondUser, date, status);
    }

    private Friendship findFriendship (Tuple<User> identifier) throws SQLException {
        String sql = String.format("select * from Friendships where id_user1 = %d " +
                        "and id_user2 = %d or id_user1 = %d and id_user2 = %d",
                identifier.getFirst().getId(), identifier.getSecond().getId(),
                identifier.getSecond().getId(), identifier.getFirst().getId());
        Connection connection = DriverManager.getConnection(url, username, password);
        var resultSet = connection.createStatement().executeQuery(sql);
        connection.close();
        if(resultSet.next())
            return extractFriendship(resultSet);
        return null;
    }

    @Override
    public Optional<Friendship> findOne(Tuple<User> friendshipTuple) {
        Friendship friendship = null;
        try{
            friendship = findFriendship(friendshipTuple);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(friendship);
    }

    @Override
    public Iterable<Friendship> findAll() {
        String sql = "select * from Friendships";
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                friendships.add(extractFriendship(resultSet));
        } catch (SQLException ignored) {}
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        try{
            Friendship friendship;
            if((friendship = findFriendship(entity.getId())) != null)
                return Optional.of(friendship);
        } catch (SQLException ignored){}
        String sql = String.format("insert into Friendships (id_user1, id_user2, date) values ('%s', '%s', '%s');",
                entity.getId().getFirst().getId(), entity.getId().getSecond().getId(), entity.getDate());
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException message) {
            if(message.getMessage().contains("violates foreign key constraint"))
                throw new RepositoryException("The users must exist.");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Tuple<User> friendshipId) {
        Friendship friendship = null;
        try{
            if((friendship = findFriendship(friendshipId)) == null)
                return Optional.empty();
        } catch (SQLException ignored) {}
        var id_user1 = friendshipId.getFirst().getId();
        var id_user2 = friendshipId.getSecond().getId();
        String sql = String.format("delete from Friendships where id_user1 = %d and id_user2 = %d or id_user1 = %d and id_user2 = %d;", id_user1, id_user2, id_user2, id_user1);
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(friendship);
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        Friendship friendship = null;
        try{
            friendship = findFriendship(entity.getId());
        } catch (SQLException ignored) {}
        var id_user1 = entity.getId().getFirst().getId();
        var id_user2 = entity.getId().getSecond().getId();
        var date = LocalDate.now();
        String sql = String.format("update Friendships set date = '%s', status = '%s' where id_user1 = %d and id_user2 = %d or id_user1 = %d and id_user2 = %d",
                date, entity.getStatus(), id_user1, id_user2, id_user2, id_user1);
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(friendship);
    }

    @Override
    public List<User> findAllRequestsForAnUser(User user) {
        String sql = String.format("select u.id_user, u.first_name, u.last_name, u.email, u.picture_path from friendships inner join users u on friendships.id_user1 = u.id_user where friendships.id_user2 = %d and friendships.status like 'pending'", user.getId());
        List<User> requests = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                requests.add(extractUserForDisplay(resultSet));
        } catch (SQLException ignored){}
        return requests;
    }

    @Override
    public List<Friendship> findAllRequestsForAUser(User user) {
        String sql = "select * from Friendships where id_user2 = " + user.getId() + " and status like 'pending';";
        List<Friendship> friendships = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                friendships.add(extractFriendship(resultSet));
        } catch (SQLException ignored) {}
        return friendships;
    }

    @Override
    public List<User> findAllRequestsSendByAUser(User user) {
        String sql = String.format("select u.id_user, u.first_name, u.last_name, u.email, u.picture_path from Users u inner join friendships f on u.id_user = f.id_user2 where id_user1 = %d and status like 'pending'", user.getId());
        List<User> requests = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                requests.add(extractUserForDisplay(resultSet));
        }catch (SQLException ignored) {}
        return requests;
    }

    @Override
    public List<User> findAllFriendsForAnUser(User user) {
        String sql1 = String.format("select u.id_user, u.first_name, u.last_name, u.email, u.picture_path from friendships inner join users u on friendships.id_user1 = u.id_user where friendships.id_user2 = %d and friendships.status like 'approved'", user.getId());
        String sql2 = String.format("select u.id_user, u.first_name, u.last_name, u.email, u.picture_path from friendships inner join users u on friendships.id_user2 = u.id_user where friendships.id_user1 = %d and friendships.status like 'approved'", user.getId());
        List<User> friends = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql1);
            while(resultSet.next())
                friends.add(extractUserForDisplay(resultSet));
            resultSet = connection.createStatement().executeQuery(sql2);
            while(resultSet.next())
                friends.add(extractUserForDisplay(resultSet));
        }catch (SQLException ignored) {}
        return friends;
    }

    @Override
    public Boolean requestBetweenTwoUsers(User user1, User user2) {
        var sql = String.format("select * from friendships where (id_user1 = %d and id_user2 = %d) or (id_user2 = %d and id_user1 = %d);", user1.getId(), user2.getId(), user1.getId(), user2.getId());
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            if(resultSet.next())
                return true;
        }catch (SQLException ignored) {}
        return false;
    }

}