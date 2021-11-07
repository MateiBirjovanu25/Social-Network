package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDatabase extends InMemoryRepository<Tuple<Long,Long>,Friendship> {

    private String url;
    private String username;
    private String password;


    /**
     * @param validator -> must not be null
     */
    public FriendshipDatabase(Validator<Friendship> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        loadData();
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long friendId1 = resultSet.getLong("friend_id1");
                Long friendId2 = resultSet.getLong("friend_id2");
                String date = resultSet.getString("date");

                Friendship friendship = new Friendship(friendId1, friendId2);
                friendship.setDate(date);
                friendships.add(friendship);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship findOne(Tuple<Long,Long> id){

        if(super.findOne(id) == null)
            return null;

        Friendship friendship = new Friendship();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships where friend_id1 = ? and friend_id2 = ?");
            statement.setLong(1,id.getE1());
            statement.setLong(2,id.getE2());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Long friend_id1 = resultSet.getLong("friend_id1");
            Long friend_id2 = resultSet.getLong("friend_id2");
            String date = resultSet.getString("date");

            friendship = new Friendship(friend_id1,friend_id2);
            friendship.setDate(date);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendship;
    }

    private void loadData(){
        Iterable<Friendship> friendships = findAll();
        friendships.forEach(super::save);
    }

    @Override
    public Friendship save(Friendship entity) {

        if (super.save(entity) != null) {
            return entity;
        }

        String sql = "insert into friendships (friend_id1, friend_id2, date) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFriendId1());
            ps.setLong(2, entity.getFriendId2());
            ps.setString(3, entity.getDate());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) {

        Friendship removedFriendship = super.delete(id);
        if (removedFriendship == null) {
            return null;
        }

        String sql = "delete from friendships where friend_id1 = ? and friend_id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id.getE1());
            ps.setLong(2, id.getE2());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return removedFriendship;
    }

}


















