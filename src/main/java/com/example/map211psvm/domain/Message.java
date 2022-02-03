package com.example.map211psvm.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long>{

    User fromUser;
    List<User> toUser;
    String date;
    String content;
    Message reply;

    public Message(User fromUser, List<User> toUser,String content) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.date = LocalDateTime.now().toString();
        this.content = content;
        this.reply = null;
    }

    public Message(User fromUser, List<User> toUser,String content, String date) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.date = date;
        this.content = content;
        this.reply = null;
    }

    public Message(User fromUser, List<User> toUser, String content, Message reply) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.reply = reply;
        this.date = LocalDateTime.now().toString();
    }

    public Message(Long id,User fromUser, List<User> toUser, String content,String date , Message reply) {
        setId(id);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.date = date;
        this.content = content;
        this.reply = reply;
    }

    public Message(Long id, User fromUser, List<User> toUser, String content, String date) {
        setId(id);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.date = date;
        this.content = content;
    }

    @Override
    public String toString() {
        return "from: "+fromUser+"      "+content;
    }

    public User getFromUser() {
        return fromUser;
    }

    public List<User> getToUser() {
        return toUser;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public Message getReply() {
        return reply;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(List<User> toUser) {
        this.toUser = toUser;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }


}
