package com.example.map211psvm.services;

import com.example.map211psvm.domain.*;
import com.example.map211psvm.domain.enums.Events;
import com.example.map211psvm.utils.RSAEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class SuperService implements PropertyChangeListener {
    public UserService userService;
    public FriendshipService friendshipService;
    public CommunityService communityService;
    public EventService eventService;
    public MessageService messageService;
    public Page page;
    public RSAEncryption encryption;
    public PropertyChangeSupport propertyChangeSupport;

    public SuperService(UserService userService, FriendshipService friendshipService, CommunityService communityService) throws NoSuchAlgorithmException {
        this.userService = userService;
        this.friendshipService  = friendshipService;
        this.communityService = communityService;
        this.encryption = new RSAEncryption();
    }

    public SuperService(UserService userService, FriendshipService friendshipService, CommunityService communityService, EventService eventService) throws NoSuchAlgorithmException {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.communityService = communityService;
        this.eventService = eventService;
        this.encryption = new RSAEncryption();
    }

    public SuperService(UserService userService, FriendshipService friendshipService, CommunityService communityService, EventService eventService, MessageService messageService) throws NoSuchAlgorithmException {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.communityService = communityService;
        this.eventService = eventService;
        this.messageService = messageService;
        this.encryption = new RSAEncryption();
        friendshipService.addPropertyChangeListener(this);
        eventService.addPropertyChangeListener(this);
        messageService.addPropertyChangeListener(this);
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Page createPage(User user) {
        page = new Page();
        page.setUser(user);
        setEventList(user);
        setFriendList(user);
        setFriendRequestPage(user);
        setMessageList(user);
        return page;
    }

    public void setFriendRequestPage(User user) {
        page.setRequestsList(friendshipService.findAllRequestsForAUserFriendships(user));
    }

    public void setFriendList(User user) {
        user = userService.findOne(user.getId()).get();
        page.setFriendsList(user.getFriendList());
    }

    public void setMessageList(User user) {
        var messages = StreamSupport
                .stream(messageService.findAll().spliterator(), false)
                .filter(message -> message.getToUser().contains(user))
                .toList();
        page.setMessagesList(messages);
    }

    public void setEventList(User user) {
        page.setEventList(eventService.findAll());
    }

    public List<User> newFriends(LocalDate dateFrom, LocalDate dateTO, User user){
        List<Friendship> friendships = new ArrayList<>();
        friendshipService.findAll().forEach(friendships::add);
        List<User> friends = new ArrayList<>();
        for(Friendship friendship : friendships){
            if(Objects.equals(friendship.getStatus(), "approved") && (friendship.getDate().isAfter(dateFrom) || friendship.getDate().isEqual(dateFrom))
                    && (friendship.getDate().isBefore(dateTO) || friendship.getDate().isEqual(dateTO)))
            {
                if(friendship.getId().getFirst().getId() == user.getId())
                    friends.add(friendship.getId().getSecond());
                else if(friendship.getId().getSecond().getId() == user.getId())
                    friends.add(friendship.getId().getFirst());
            }
        }
        return friends;
    }

    public List<Message> newMessages(LocalDate dateFrom, LocalDate dateTO, User user) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        List<Message> messages = new ArrayList<>();
        List<Message> newMessages = new ArrayList<>();
        messageService.findAll().forEach(messages::add);

        for(Message message : messages){
            String messageString = message.getDate().split("T")[0];
            LocalDate messageDate = LocalDate.parse(messageString);
            if((messageDate.isAfter(dateFrom) || messageDate.isEqual(dateFrom))
                    && messageDate.isBefore(dateTO) || messageDate.isEqual(dateTO))
            {
                if(message.getToUser().contains(new User(user.getId(),null,null,null,null)))
                {
                    String key = userService.readUserKey(user);
                    String decodedText = encryption.decode(message.getContent(),key);
                    message.setContent(decodedText);
                    newMessages.add(message);
                }

            }
        }
        return newMessages;
    }

    public Predicate<Message> predicateForReceivedMessages(Long id_user1, Long id_user2){
        return (message -> (message.getToUser().contains(new User(id_user1,null,null,null,null)) && (message.getFromUser().getId().equals(id_user2))));
    }

    public List<Message> messagesReceivedByaUser(Long idReceiver,Long idSender) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        List<Message> messages = new ArrayList<>();
        messageService.findAll().forEach(messages::add);
        List<Message> conversation =  messages
                .stream()
                .filter(predicateForReceivedMessages(idReceiver,idSender))
                .sorted(Comparator.comparing(x -> LocalDateTime.parse(x.getDate())))
                .toList();

        Optional<User> user = userService.findOne(idReceiver);
        String key = userService.readUserKey(user.get());
        for(Message message : conversation){
            String decodedText = encryption.decode(message.getContent(),key);
            message.setContent(decodedText);
        }
        return conversation;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Events.Event.toString()))
            setEventList(page.getUser());
        if(evt.getPropertyName().equals(Events.Message.toString()))
            setMessageList(page.getUser());
        if(evt.getPropertyName().equals(Events.FriendRequest.toString()))
            setFriendRequestPage(page.getUser());
        if(evt.getPropertyName().equals(Events.Friends.toString()))
            setFriendList(page.getUser());
        propertyChangeSupport.firePropertyChange("Total", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }
}
