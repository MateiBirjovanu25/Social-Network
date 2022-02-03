package com.example.map211psvm.domain.dtos;

import com.example.map211psvm.utils.Constants;
import javafx.scene.control.Button;

import java.time.LocalDate;

public class UserFriendshipDto {
    private String firstName;
    private String lastName;
    private LocalDate localDate;
    private Long id;
    private String status;
    private String email;

    public UserFriendshipDto(String firstName, String lastName, LocalDate localDate, Long id, String status, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.localDate = localDate;
        this.id = id;
        this.status = status;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDate() {
        return localDate;
    }

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public UserFriendshipDto(String firstName, String lastName, LocalDate localDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return firstName + " | " + lastName + " | " + localDate.format(Constants.DATE_TIME_FORMATTER);
    }
}
