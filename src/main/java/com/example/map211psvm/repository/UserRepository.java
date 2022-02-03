package com.example.map211psvm.repository;

import com.example.map211psvm.domain.User;

import java.sql.*;
import java.util.*;

public class UserRepository implements Repository<Long, User>{
    private String url;
    private String username;
    private String password;

    public UserRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private void addFriendsOfUser(User user){
        String sql = String.format("select distinct U.id_user, U.first_name, U.last_name, U.email, U.password, U.public_key, U.picture_path " +
                "from Users U inner join " +
                "Friendships F on F.id_user1 = U.id_user or F.id_user2 = U.id_user " +
                "where (F.id_user2 = %d or F.id_user1 = %d) and U.id_user != %d and F.status like 'approved';", user.getId(), user.getId(), user.getId());
        List<User> friends = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                friends.add(extractUser(resultSet));
        } catch (SQLException ignored) {ignored.printStackTrace();}
        user.setFriendList(friends);
    }

    private User extractUser(ResultSet resultSet) throws SQLException{
        var userId = resultSet.getLong("id_user");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var email = resultSet.getString("email");
        var password = resultSet.getString("password");
        var publicKey = resultSet.getString("public_key");
        var photoPath = resultSet.getString("picture_path");
        return new User(userId, firstName, lastName, email, password, publicKey, photoPath);
    }

    private User findUser (String identifier) throws SQLException {
        String sql = String.format("select * from Users where email = '%s'", identifier);
        Connection connection = DriverManager.getConnection(url, username, password);
        var resultSet = connection.createStatement().executeQuery(sql);
        if(resultSet.next())
            return extractUser(resultSet);
        return null;
    }

    private User findUser (Long identifier) throws SQLException {
        String sql = String.format("select * from Users where id_user = %d", identifier);
        Connection connection = DriverManager.getConnection(url, username, password);
        var resultSet = connection.createStatement().executeQuery(sql);
        if(resultSet.next())
            return extractUser(resultSet);
        return null;
    }

    @Override
    public Optional<User> findOne(Long id) {
        User user = null;
        try{
            user = findUser(id);
            if(user != null)
                addFriendsOfUser(user);
        } catch (SQLException ignored){}
        return Optional.ofNullable(user);
    }

    public Optional<User> findOne(String email) {
        User user = null;
        try{
            user = findUser(email);
            if(user != null)
                addFriendsOfUser(user);
        } catch (SQLException ignored){}
        return Optional.ofNullable(user);
    }

    @Override
    public Iterable<User> findAll() {
        String sql = "select * from Users";
        Map<Long, User> users = new HashMap<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()){
                var user = extractUser(resultSet);
                users.put(user.getId(), user);
                addFriendsOfUser(user);
            }
        } catch (SQLException ignored) {}
        return users.values();
    }

    @Override
    public Optional<User> save(User entity) {
        String sql = String.format("insert into Users (first_name, last_name, email, password, public_key, picture_path) values ('%s', '%s', '%s', '%s', '%s', '%s');", entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPassword(), entity.getPublicKey(), entity.getPhotoPath());
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException error) {
            if(error.getMessage().contains("No results"))
                return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<User> delete(Long id) {
        User user = null;
        try{
            if((user = findUser(id)) == null)
                return Optional.empty();
        } catch (SQLException ignored) {}
        String sql = String.format("delete from Users where id_user = %d", id);
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> update(User entity) {
        User user = null;
        try{
            user = findUser(entity.getId());
        } catch (SQLException ignored) {}
        String sql = String.format("update Users set first_name = '%s', last_name = '%s', email = '%s', password = '%s' where id_user = %d", entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPassword(),entity.getId());
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(user);
    }

    public List<User> findAllStartWith(String name, User user) {
        String sql = String.format("and id_user != %d", user.getId());
        sql = "select * from Users where first_name like '" + name +"%' or last_name like '" + name + "%' " + sql;
        List<User> users = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()){
                users.add(extractUser(resultSet));
            }
        } catch (SQLException ignored) {}
        return users;
    }
}
