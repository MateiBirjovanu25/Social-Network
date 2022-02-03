package com.example.map211psvm.repository;

import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;

import java.sql.*;
import java.util.*;

public class EventRepository implements RepositoryEvent{
    private String url;
    private String username;
    private String password;

    public EventRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Event extractEvent(ResultSet resultSet) throws SQLException {
        var eventId = resultSet.getLong("id_event");
        var name = resultSet.getString("name");
        var description = resultSet.getString("description");
        var date = resultSet.getTimestamp("date");
        var id_user = resultSet.getLong("id_user");
        var first_name = resultSet.getString("first_name");
        var last_name = resultSet.getString("last_name");
        var email = resultSet.getString("email");
        var organizer = new User(id_user, first_name, last_name, email);
        var event =  new Event(eventId, name, description, organizer, date.toLocalDateTime());
        addParticipants(event);
        return event;
    }

    private Event extractEventNoParticipants(ResultSet resultSet) throws SQLException {
        var eventId = resultSet.getLong("id_event");
        var name = resultSet.getString("name");
        var description = resultSet.getString("description");
        var date = resultSet.getTimestamp("date");
        var id_user = resultSet.getLong("id_user");
        var first_name = resultSet.getString("first_name");
        var last_name = resultSet.getString("last_name");
        var email = resultSet.getString("email");
        var organizer = new User(id_user, first_name, last_name, email);
        var event =  new Event(eventId, name, description, organizer, date.toLocalDateTime());
        return event;
    }

    @Override
    public Optional<Event> findOne(Long id) {
        String sql = String.format("select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer where id_event = %d", id);
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next())
                return Optional.of(extractEvent(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Event> findAll() {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer;";
        Set<Event> eventSet = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                eventSet.add(extractEvent(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventSet;
    }

    @Override
    public Optional<Event> save(Event entity) {
        String sql = String.format("insert into events (name, description, organizer, date) values ('%s', '%s', %d, ?);", entity.getName(), entity.getDescription(), entity.getOrganizer().getId());
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement(sql);
            Timestamp timestamp = Timestamp.valueOf(entity.getDateTime());
            statement.setTimestamp(1, timestamp);
            statement.executeQuery();
        } catch (SQLException error) {
            if(error.getMessage().contains("No results"))
                return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Event> delete(Long id) {
        var eventOptional = findOne(id);
        if(eventOptional.isEmpty())
            return eventOptional;
        String sql = String.format("delete from Events where id_event = %d", id);
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return eventOptional;
    }

    @Override
    public Optional<Event> update(Event entity) {
        var eventOptional = findOne(entity.getId());
        if(eventOptional.isEmpty())
            return eventOptional;
        String sql = String.format("update Events set name = '%s', description = '%s', organizer = %d, date = ? where id_event = %d;", entity.getName(), entity.getDescription(), entity.getOrganizer().getId(), entity.getId());
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getDateTime()));
            preparedStatement.executeQuery();
        } catch (SQLException ignored) {}
        return eventOptional;
    }

    @Override
    public Long addUserToEvent(Long eventId, Long userId) {
         String sql = "insert into events_users (id_user, id_event) values (%d, %d)";
         sql = String.format(sql, userId, eventId);
         try(Connection connection = DriverManager.getConnection(url, username, password)){
             connection.createStatement().executeQuery(sql);
         } catch (SQLException e) {
             if(e.getMessage().contains("already exists"))
                 return userId;
         }
         return null;
    }

    @Override
    public Long removeUserToEvent(Long eventId, Long userId) {
        String sql = "delete from events_users where id_user = %d and id_event = %d";
        sql = String.format(sql, userId, eventId);
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored) {}
        return userId;
    }

    @Override
    public List<Event> findAllList() {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer;";
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                eventList.add(extractEvent(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    @Override
    public List<Event> eventMadeByAUser(User user) {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer where e.organizer = %d;";
        sql = String.format(sql, user.getId());
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                eventList.add(extractEventNoParticipants(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    @Override
    public List<Event> eventNotMadeByAUser(User user) {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer where e.organizer != %d;";
        sql = String.format(sql, user.getId());
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                eventList.add(extractEvent(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public List<Event> findAllEventsWithNoParticipants() {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer;";
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()) {
                var eventId = resultSet.getLong("id_event");
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");
                var date = resultSet.getTimestamp("date");
                var id_user = resultSet.getLong("id_user");
                var first_name = resultSet.getString("first_name");
                var last_name = resultSet.getString("last_name");
                var email = resultSet.getString("email");
                var organizer = new User(id_user, first_name, last_name, email);
                eventList.add(new Event(eventId, name, description, organizer, date.toLocalDateTime()));
            }
        } catch (SQLException ignored) {}
        return eventList;
    }

    @Override
    public List<Event> findSomeNotMadeByAUser(User user, int offset, int limit) {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer where e.organizer != %d offset %d limit %d;";
        sql = String.format(sql, user.getId(), offset, limit);
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                eventList.add(extractEvent(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    @Override
    public List<Event> findSomeMadeByAUser(User user, int offset, int limit) {
        String sql = "select e.id_event, e.name, e.description, e.date, u.id_user, u.first_name, u.last_name, u.email from events e inner join users u on u.id_user = e.organizer where e.organizer = %d offset %d limit %d;";
        sql = String.format(sql, user.getId(), offset, limit);
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                eventList.add(extractEvent(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    @Override
    public int noOfEventsMadeByAUser(User user) {
        var sql = "select count(*) from events where organizer = " + user.getId();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
           var resultSet = connection.createStatement().executeQuery(sql);
           if(resultSet.next())
               return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int noOfEventsNotMadeByAUser(User user) {
        var sql = "select count(*) from events where organizer != " + user.getId();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            if(resultSet.next())
                return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setNotifications(Long userId, Long eventId, Boolean notificationsStatus) {
        String sql = "update events_users set notifications = ? where id_event = ? and id_user = ?;";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, notificationsStatus);
            preparedStatement.setLong(2, eventId);
            preparedStatement.setLong(3, userId);
            preparedStatement.executeQuery();
        } catch (SQLException ignored) {}
    }

    @Override
    public List<Event> eventsWhereIsAUserOrdered(User user, int offset, int limit) {
        String sql = String.format("select * from events e inner join events_users eu on e.id_event = eu.id_event where id_user = %d order by date offset %d limit %d;", user.getId(), offset, limit);
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()) {
                var idEvent = resultSet.getLong("id_event");
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");
                var date = resultSet.getTimestamp("date").toLocalDateTime();
                var organizer = new User(resultSet.getLong("organizer"), null, null, null);
                eventList.add(new Event(idEvent, name, description, organizer, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        var userId = resultSet.getLong("id_user");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var email = resultSet.getString("email");
        return new User(userId, firstName, lastName, email);
    }

    private void addParticipants(Event event){
        String sql = "select u.id_user, u.first_name, u.last_name, u.email, eu.notifications from users u inner join events_users eu on u.id_user = eu.id_user where eu.id_event = %d";
        sql = String.format(sql, event.getId());
        Map<User, Boolean> participantsList = new HashMap<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            var resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next())
                participantsList.put(extractUser(resultSet), resultSet.getBoolean("notifications"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        event.setParticipants(participantsList);
    }
}

