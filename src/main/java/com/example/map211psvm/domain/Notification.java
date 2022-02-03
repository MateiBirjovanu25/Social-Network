package com.example.map211psvm.domain;

import com.example.map211psvm.domain.enums.Notifications;

import java.time.LocalDate;

public class Notification {

    private String name;
    private String description;
    private Notifications type;
    private LocalDate dateTime;

    public Entity<?> getEntity() {
        return entity;
    }

    public void setEntity(Entity<?> entity) {
        this.entity = entity;
    }

    private Entity<?> entity;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Notifications getType() {
        return type;
    }

    public void setType(Notifications type) {
        this.type = type;
    }

    public Notification(String name, String description, Notifications type, Entity<?> entity) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.entity = entity;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public Notification(String name, String description, Notifications type, LocalDate dateTime, Entity<?> entity) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.dateTime = dateTime;
        this.entity = entity;
    }
}
