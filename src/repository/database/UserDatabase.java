package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.*;

public class UserDatabase extends InMemoryRepository<Long,User> {

    private String url;
    private String username;
    private String password;


    /**
     * @param validator -> must not be null
     */
    public UserDatabase(Validator<User> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        loadData();
    }

    @Override
    public Iterable<User> findAll(){
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from users");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Long id = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = new User(firstName,lastName);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public User findOne(Long id){

        if(super.findOne(id) == null)
            return null;

        User user = new User();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from users where user_id = ?");
            statement.setLong(1,id);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Long user_id = resultSet.getLong("user_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            user = new User(firstName,lastName);
            user.setId(user_id);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    private void loadData() {
        Iterable<User> users = findAll();
        users.forEach(super::save);
    }


    @Override
    public User save(User entity) {

        if(super.save(entity) != null) {
            return entity;
        }

        String sql = "insert into users (first_name, last_name ) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id){

        User removedUser = super.delete(id);
        if(removedUser == null)
        {
            return null;
        }

        String sql = "delete from users where user_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return removedUser;
    }

    @Override
    public User update(User entity) {

        User updatedUser = super.update(entity);
        if (updatedUser != null)
        {
            return entity;
        }


        String sql = "update users set first_name=? , last_name=?  where user_id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setInt(3,entity.getId().intValue());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


















