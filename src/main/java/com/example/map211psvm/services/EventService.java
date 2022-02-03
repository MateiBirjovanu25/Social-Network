package com.example.map211psvm.services;

import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.repository.RepositoryEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Optional;

public class EventService {
    private RepositoryEvent repositoryEvent;
    private PropertyChangeSupport propertyChangeSupport;

    public EventService(RepositoryEvent repositoryEvent) {
        this.repositoryEvent = repositoryEvent;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Optional<Event> findOne(Long id) {
        return this.repositoryEvent.findOne(id);
    }

    public List<Event> findAll() {
        return this.repositoryEvent.findAllList();
    }

    public Optional<Event> save(Event entity) {
        var savedEvent = this.repositoryEvent.save(entity);
        savedEvent.ifPresentOrElse(x -> {}, () -> propertyChangeSupport.firePropertyChange("Added a new Event", null, entity));
        return savedEvent;
    }

    public Optional<Event> delete(Long id) {
        var deleted = this.repositoryEvent.delete(id);
        deleted.ifPresent(x -> propertyChangeSupport.firePropertyChange("Deleted an event", x, null));
        return deleted;
    }

    public Optional<Event> update(Event entity) {
        var updated = this.repositoryEvent.update(entity);
        updated.ifPresent(x -> propertyChangeSupport.firePropertyChange("Updated an event", null, entity));
        return updated;
    }

    public Long addUserToEvent(Long eventId, Long userId) {
        var added = this.repositoryEvent.addUserToEvent(eventId, userId);
        if(added == null)
            propertyChangeSupport.firePropertyChange("Added a new user to the event", null, added);
        return added;
    }

    public Long removeUserToEvent(Long eventId, Long userId) {
        var deleted = this.repositoryEvent.removeUserToEvent(eventId, userId);
        if(deleted != null)
            propertyChangeSupport.firePropertyChange("Deleted a participant", null, deleted);
        return deleted;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    public List<Event> eventsMadeByAUser(User user){
        return repositoryEvent.eventMadeByAUser(user);
    }

    public List<Event> eventsNotMadeByAUser(User user) {
        return repositoryEvent.eventNotMadeByAUser(user);
    }

    public List<Event> someEventsNotMadeByAUser(User user, int offset, int limit) {
        return repositoryEvent.findSomeNotMadeByAUser(user, offset, limit);
    }

    public List<Event> someEventsMadeByAUser(User user, int offset, int limit) {
        return repositoryEvent.findSomeMadeByAUser(user, offset, limit);
    }

    public int noOfEventsMadeByAUser(User user) {
        return repositoryEvent.noOfEventsMadeByAUser(user);
    }

    public int noOfEventsNotMadeByAUser(User user) {
        return repositoryEvent.noOfEventsNotMadeByAUser(user);
    }

    public void setNotifications(Long userId, Long eventId, Boolean notificationsStatus) {
        repositoryEvent.setNotifications(userId, eventId, notificationsStatus);
    }

    public List<Event> eventsWhereIsAUserOrdered(User user, int offset, int limit) {
        return repositoryEvent.eventsWhereIsAUserOrdered(user, offset, limit);
    }
}
