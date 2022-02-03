package com.example.map211psvm.domain;

import java.util.List;

public class Page {

    private List<Friendship> requestsList;
    private List<User> friendsList;
    private List<Message> messagesList;
    private List<Event> eventList;
    private User user;

    public Page() {}
    public Page(User user, List<Friendship> requestsList, List<Message> messagesList, List<Event> eventList) {
        this.user = user;
        this.requestsList = requestsList;
        this.messagesList = messagesList;
        this.eventList = eventList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Friendship> getRequestsList() {
        return requestsList;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setRequestsList(List<Friendship> requestsList) {
        this.requestsList = requestsList;
    }

    public void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    public void setMessagesList(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
