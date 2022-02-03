package com.example.map211psvm.repository;

import com.example.map211psvm.domain.Message;
import com.example.map211psvm.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class MessageRepository implements Repository<Long, Message>{

    private String url;
    private String username;
    private String password;

    public MessageRepository(String url, String username, String password) {
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
        return new User(userId, firstName, lastName, email);
    }

    private List<User> getToUsers(Long messageId, Long userId) throws SQLException{
        String sql = String.format("select user_id from to_users t " +
                                   "inner join messages m on t.message_id = m.message_id " +
                                   "where m.from_user = %d and m.message_id = %d",
                                    userId,messageId);
        Connection connection = DriverManager.getConnection(url,username,password);
        var resultSet = connection.createStatement().executeQuery(sql);
        ArrayList<User> toUsers = new ArrayList<>();

        while(resultSet.next()){
            Long toUserId = resultSet.getLong("user_id");
            User toUser = findUser(toUserId);
            toUsers.add(toUser);
        }
        connection.close();
        return toUsers;
    }

    private Message extractMessage(ResultSet resultSet) throws SQLException {
        var messageId = resultSet.getLong("message_id");
        var fromUserId = resultSet.getLong("from_user");
        var date = resultSet.getString("date");
        var content = resultSet.getString("content");
        var replyId = resultSet.getLong("is_reply");
        //var isReply = resultSet.getLong("is_reply");
        User fromUser = findUser(fromUserId);
        Message reply = findMessage(replyId);
        return new Message(messageId, fromUser, getToUsers(messageId,fromUserId), content, date,reply);
    }

    private Message findMessage(Long identifier) throws SQLException{
        String sql = String.format("select * from Messages where message_id = %d",identifier);
        Connection connection = DriverManager.getConnection(url,username,password);
        var resultSet = connection.createStatement().executeQuery(sql);
        if(resultSet.next())
            return extractMessage(resultSet);
        return null;
    }

    private Long getMessageId() throws SQLException{
        String sql = "select max(message_id) as id from Messages";
        Connection connection = DriverManager.getConnection(url,username,password);
        var resultSet = connection.createStatement().executeQuery(sql);
        if(resultSet.next()) {
            Long id = resultSet.getLong("id");
            return id;
        }
        return 0L;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        Message message = null;
        try {
            message = findMessage(id);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(message);
    }

    @Override
    public Iterable<Message> findAll() {
        String sql = "select * from Messages";
        Set<Message> messages = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                messages.add(extractMessage(resultSet));
            connection.close();
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        String sqlString;
        String sql;
        if(entity.getReply() == null)
            sqlString = "insert into messages (from_user, date, content) values ('%s', '%s', '%s');";
        else{
            sqlString = "insert into messages (from_user, date, content, is_reply) values ('%s', '%s','%s','%s');";
            if(!entity.getReply().getToUser().contains(entity.getFromUser()))
                throw new RepositoryException("User did not receive that message");
        }
        if (entity.getReply() == null)
            sql = String.format(sqlString,
                    entity.getFromUser().getId(), entity.getDate(), entity.getContent());
        else
            sql = String.format(sqlString,
                    entity.getFromUser().getId(), entity.getDate(), entity.getContent(), entity.getReply().getId());

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
            connection.close();
        }catch (SQLException error) {
            if(error.getMessage().contains("violates foreign key constraint"))
                throw new RepositoryException("The users must exist.");
        }

        Long newMessageId=0L;
        try {
            newMessageId = getMessageId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(newMessageId);

        for(User user : entity.getToUser()) {
            sqlString = "insert into To_users (message_id,user_id) values ('%s', '%s');";
            sql = String.format(sqlString,newMessageId,user.getId());
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                connection.createStatement().executeQuery(sql);
                connection.close();
            }catch (SQLException error) {
                if(error.getMessage().contains("violates foreign key constraint"))
                    throw new RepositoryException("The users must exist.");
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Long messageId) {
        Message message = null;
        try{
            if((message = findMessage(messageId)) == null)
                return Optional.empty();
        } catch (SQLException ignored) {}
        String sql = String.format("delete from Messages where message_id = %d",messageId);
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return Optional.ofNullable(message);
    }

    @Override
    public Optional<Message> update(Message entity) {
        Message message = null;
        try{
            message = findMessage(entity.getId());
        } catch (SQLException ignored) {}
        var date = LocalDate.now();
        //User user = entity.getToUser().get(0); //asta e singurul user din lista
        String sql = String.format("update Messages set is_reply='%s' where message_id=%d",
                entity.getReply().getId());
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException error) {
            if(error.getMessage().contains("violates foreign key constraint"))
                throw new RepositoryException("The users must exist.");
        }
        return Optional.ofNullable(message);
    }
}
