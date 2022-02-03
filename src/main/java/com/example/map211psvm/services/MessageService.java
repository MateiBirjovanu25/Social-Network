package com.example.map211psvm.services;

import com.example.map211psvm.domain.Message;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.domain.validators.Validator;
import com.example.map211psvm.repository.Repository;
import com.example.map211psvm.utils.RSAEncryption;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class MessageService {
    Repository<Long, Message> repository;
    Validator<Message> validator;
    private PropertyChangeSupport support;
    private Message lastMessage;
    private RSAEncryption encryption;

    public MessageService(Repository<Long, Message> repository, Validator<Message> validator) throws NoSuchAlgorithmException {
        support = new PropertyChangeSupport(this);
        this.repository = repository;
        this.validator = validator;
        this.encryption = new RSAEncryption();
    }

    public Optional<Message> findOne(Long messageId){
        return repository.findOne(messageId);
    }

    public  Iterable<Message> findAll(){
        return repository.findAll();
    }

    public Optional<Message> save(Long fromUserId, List<Long> toUserIds, String content){
        var fromUser = new User(fromUserId);
        var users = toUserIds
                .stream().map(User::new)
                .toList();
        Message message = new Message(fromUser,users,content);
        validator.validate(message);
        Optional<Message> verifyMessage =  repository.save(message);
        support.firePropertyChange("message sent",message,null);
        return verifyMessage;
    }

    public Optional<Message> saveReply(Long fromUserId, String content, Long replyId){
        var fromUser = new User(fromUserId);
        Message reply = findOne(replyId).get();
        List<User> users = new ArrayList<>();
        users.add(reply.getFromUser());
        Message message = new Message(fromUser,users,content,reply);
        validator.validate(message);
        Optional<Message> sentMessage = repository.save(message);
        support.firePropertyChange("message sent",message,null);
        return sentMessage;
    }

    public Optional<Message> saveReplyAll(Long fromUserId,String content, Long replyId){
        var fromUser = new User(fromUserId);
        Message reply = findOne(replyId).get();
        List<User> users = new ArrayList<>();
        users.add(reply.getFromUser());
        Message replyMessage  = findOne(replyId).get();
        for(User user : replyMessage.getToUser()){
            if(user.getId() != fromUserId){
                users.add(user);
            }
        }
        Message message = new Message(fromUser,users,content,reply);
        validator.validate(message);
        return repository.save(message);
    }

    public Optional<Message> delete(Long messageId){
        return repository.delete(messageId);
    }

    public Optional<Message> update(Long fromUserId, List<Long> toUserIds, String content){
        List<User> users = new ArrayList<>();
        for(Long id : toUserIds){
            var toUser = new User(id);
            users.add(toUser);
        }
        var fromUser = new User(fromUserId);
        var message = new Message(fromUser,users,content);
        validator.validate(message);
        return repository.update(message);
    }

    public Predicate<Message> predicateForFindingAllMessagesBetweenTwoUsers(Long id_user1, Long id_user2){
        return (message -> (message.getToUser().size() == 1) && (message.getFromUser().getId().equals(id_user1) && message.getToUser().contains(new User(id_user2)) || message.getFromUser().getId().equals(id_user2) && message.getToUser().contains(new User(id_user1))));
    }

    public List<Message> showAConversationBetweenTwoUsers(Long id_user1, Long id_user2){
        List<Message> messages = new ArrayList<>();
        repository.findAll().forEach(messages::add);
        List<Message> conversation =  messages
                .stream()
                .filter(predicateForFindingAllMessagesBetweenTwoUsers(id_user1, id_user2))
                .sorted(Comparator.comparing(x -> LocalDateTime.parse(x.getDate())))
                .toList();

        conversation.forEach(x -> lastMessage = x);
        return conversation;
    }

    public Message getLastMessage(){
        return lastMessage;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.removePropertyChangeListener(propertyChangeListener);
    }

}
