package com.example.map211psvm.repository;

import com.example.map211psvm.domain.Friendship;
import com.example.map211psvm.domain.Tuple;
import com.example.map211psvm.domain.User;

import java.util.List;

public interface RepositoryFriendship extends Repository<Tuple<User>, Friendship> {

    List<User> findAllRequestsForAnUser(User user);

    List<Friendship> findAllRequestsForAUser(User user);

    List<User> findAllRequestsSendByAUser(User user);

    List<User> findAllFriendsForAnUser(User user);

    Boolean requestBetweenTwoUsers(User user1, User user2);
}
