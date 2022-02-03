package com.example.map211psvm.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Event extends Entity<Long> {
    private String name;
    private String description;
    private User organizer;
    private LocalDateTime dateTime;
    private Map<User, Boolean> participants;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", organizer=" + organizer +
                ", dateTime=" + dateTime +
                ", participants=" + participants +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Event(Long id, String name, String description, User organizer, LocalDateTime dateTime) {
        participants = new HashMap<>();
        super.setId(id);
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.dateTime = dateTime;
    }

    public Event(String name, String description, User organizer, LocalDateTime dateTime) {
        participants = new HashMap<>();
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.dateTime = dateTime;
    }

    public Map<User, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<User, Boolean> participants) {
        this.participants = participants;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
