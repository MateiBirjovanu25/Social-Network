package com.example.map211psvm.services;

import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.Friendship;
import com.example.map211psvm.domain.Tuple;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.domain.dtos.UserFriendshipDto;
import com.example.map211psvm.domain.enums.Events;
import com.example.map211psvm.domain.validators.Validator;
import com.example.map211psvm.repository.Repository;
import com.example.map211psvm.repository.RepositoryFriendship;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FriendshipService {
    private RepositoryFriendship repository;
    private Validator<Friendship> validator;
    private PropertyChangeSupport support;

    public FriendshipService(RepositoryFriendship repository, Validator<Friendship> validator) {
        support = new PropertyChangeSupport(this);
        this.repository = repository;
        this.validator = validator;
    }

    public Optional<Friendship> findOne(Long id_user1, Long id_user2) {
        var user1 = new User(id_user1);
        var user2 = new User(id_user2);
        return repository.findOne(new Tuple<>(user1, user2));
    }

    public Iterable<Friendship> findAll() {
        return repository.findAll();
    }

    public Optional<Friendship> save(Long id_user1, Long id_user2) {
        var user1 = new User(id_user1);
        var user2 = new User(id_user2);
        var friendship = new Friendship(user1, user2);
        validator.validate(friendship);
        var savedFriendship = repository.save(friendship);
        savedFriendship.ifPresentOrElse(x -> {}, () -> support.firePropertyChange(String.valueOf(Events.FriendRequest), null, friendship));
        return savedFriendship;
    }

    public Optional<Friendship> delete(Long id_user1, Long id_user2) {
        var user1 = new User(id_user1);
        var user2 = new User(id_user2);
        var deletedFriendship = repository.delete(new Tuple<>(user1, user2));
        deletedFriendship.ifPresent(x -> support.firePropertyChange(String.valueOf(Events.Friends), x, null));
        return deletedFriendship;
    }

    public Optional<Friendship> update(Long id_user1, Long id_user2, String status) {
        var user1 = new User(id_user1);
        var user2 = new User(id_user2);
        var friendship = new Friendship(user1, user2, status);
        validator.validate(friendship);
        var result = repository.update(friendship);
        result.ifPresent(x -> support.firePropertyChange(String.valueOf(Events.Friends), x.getStatus(), status));
        return result;
    }

    public Predicate<Friendship> predicateForFindingAllFriendshipsForAnUser(User user) {
        return x -> (x.getId().getFirst().equals(user) || x.getId().getSecond().equals(user)) && x.getStatus().equals("approved");
    }

    public Predicate<Friendship> predicateForFindingAllRequestsForAnUsers(User user){
        return (x -> x.getId().getSecond().equals(user) && x.getStatus().equals("pending"));
    }

    public Predicate<Friendship> predicateFindAllFriendsFromMonth(User user,String month) {
        return (x -> x.getDate().toString().split("-")[1].equals(month) && (x.getId().getFirst().equals(user) || x.getId().getSecond().equals(user)) && x.getStatus().equals("approved"));
    }

    public List<User> findAllFriendRequestsOfAnUser(User user) {
        List<Friendship> friendships = new ArrayList<>();
        repository.findAll().forEach(friendships::add);
        return friendships
                .stream()
                .filter(predicateForFindingAllRequestsForAnUsers(user))
                .map(x -> x.getId().getFirst())
                .toList();
    }

    public List<UserFriendshipDto> findAllFriendsOfAnUser(Long userID, Predicate<Friendship> predicate) {
        ArrayList<Friendship> friendships = new ArrayList<>();
        findAll().forEach(friendships::add);
        return friendships
                .stream()
                .filter(predicate)
                .map(x -> new UserFriendshipDto(
                        x.getId().getFirst().getId().equals(userID) ? x.getId().getSecond().getFirstName() : x.getId().getFirst().getFirstName(),
                        x.getId().getFirst().getId().equals(userID) ? x.getId().getSecond().getLastName() : x.getId().getFirst().getLastName(),
                        x.getDate()))
                .toList();
    }

    public List<User> findAllFriendsOfAUser(User user) {
        return repository.findAllFriendsForAnUser(user);
    }

    public List<User> findAllRequestsForAUser(User user){
        return repository.findAllRequestsForAnUser(user);
    }

    public List<Friendship> findAllRequestsForAUserFriendships(User user) {
        return repository.findAllRequestsForAUser(user);
    }

    public List<User> findAllRequestsSendByAUser(User user) {
        return repository.findAllRequestsSendByAUser(user);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.removePropertyChangeListener(propertyChangeListener);
    }

    public Boolean requestBetweenTwoUsers(User user1, User user2) {
        return repository.requestBetweenTwoUsers(user1, user2);
    }
}
