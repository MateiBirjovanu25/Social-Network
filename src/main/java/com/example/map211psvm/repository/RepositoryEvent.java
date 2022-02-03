package com.example.map211psvm.repository;
import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;

import java.util.List;

public interface RepositoryEvent extends Repository<Long, Event> {

    public Long addUserToEvent(Long eventId, Long userId);

    public Long removeUserToEvent(Long eventId, Long userId);

    public List<Event> findAllList();

    public List<Event> eventMadeByAUser(User user);

    public List<Event> eventNotMadeByAUser(User user);

    public List<Event> findSomeNotMadeByAUser(User user, int offset, int limit);

    public List<Event> findSomeMadeByAUser(User user, int offset, int limit);

    public int noOfEventsMadeByAUser(User user);

    public int noOfEventsNotMadeByAUser(User user);

    public void setNotifications(Long userId, Long eventId, Boolean notificationsStatus);

    public List<Event> eventsWhereIsAUserOrdered(User user, int offset, int limit);
}
