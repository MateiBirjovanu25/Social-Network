package com.example.map211psvm.domain;

import com.example.map211psvm.utils.Constants;

import java.time.LocalDate;

public class Friendship extends Entity<Tuple<User>>{
    LocalDate date;
    String status;

    /** Class constructor.
     * @param firstUser first user.
     * @param secondUser second user.
     * @param status status of the friendship.
     */
    public Friendship(User firstUser, User secondUser, String status) {
        setId(new Tuple<>(firstUser, secondUser));
        this.status = status;
        this.date = LocalDate.now();
    }

    /** Class constructor.
     * @param firstUser first friend.
     * @param secondUser second friend's id.
     */
    public Friendship(User firstUser, User secondUser) {
        setId(new Tuple<>(firstUser, secondUser));
        date = LocalDate.now();
        setStatus("pending");
    }

    public Friendship(User firstUser, User secondUser, LocalDate date) {
        setId(new Tuple<>(firstUser, secondUser));
        this.date = date;
    }

    public Friendship(User firstUser, User secondUser, LocalDate date, String status) {
        setId(new Tuple<>(firstUser, secondUser));
        this.date = date;
        this.status = status;
    }

    /** Gets the status of the friendship.
     * @return the status of the friendship.
     */
    public String getStatus() {
        return status;
    }

    /** Sets the status of the friendship.
     * @param status the new status of the friendship.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getId().getFirst().getEmail() + " " + getId().getSecond().getEmail() + " " + date.format(Constants.DATE_TIME_FORMATTER);
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }

    /** Sets the date of the friendship.
     *
     * @param date the new date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
