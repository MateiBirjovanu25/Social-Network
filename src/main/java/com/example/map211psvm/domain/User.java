package com.example.map211psvm.domain;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity<Long> implements Comparable<User>{

    private String firstName;
    private String lastName;
    private String email;
    private String photoPath;
    private List<User> friendList;
    private String password;
    private String publicKey;

    public User(long id_user, String first_name, String last_name, String email) {
        setId(id_user);
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public User(){}

    public User(Long id){
        setId(id);
    }

    /** Class constructor.
     *
     * @param id the id of the user.
     * @param firstName first name of the user.
     * @param lastName last name of the user.
     * @param email the email of the user.
     */
    public User(Long id, String firstName, String lastName, String email, String photoPath) {
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friendList = new ArrayList<>();
        this.photoPath = photoPath;
    }

    public User(Long id, String firstName, String lastName, String email,String password,String publicKey, String photoPath) {
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friendList = new ArrayList<>();
        this.password = password;
        this.publicKey = publicKey;
        this.photoPath = photoPath;
    }

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friendList = new ArrayList<>();
    }

    public User(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friendList = new ArrayList<>();
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, String publicKey, String photoPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friendList = new ArrayList<>();
        this.password = password;
        this.publicKey = publicKey;
        this.photoPath = photoPath;
    }

    public User(Long id, String firstName, String lastName, String email, String password, String publicKey) {
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friendList = new ArrayList<>();
        this.password = password;
        this.publicKey = publicKey;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    /** Gets the first name of the user.
     *
     * @return the first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /** Sets the first name of the user.
     *
     * @param firstName the new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** Gets the last name of the user.
     *
     * @return the last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /** Sets the last name of the user.
     *
     * @param lastName the last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** Returns the users email.
     *
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /** Sets the user email.
     *
     * @param email the new email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Gets the friends list.
     *
     * @return the list of friends.
     */
    public List<User> getFriendList() {
        return friendList;
    }

    /** Sets the friend list.
     *
     * @param friendList the new friend list.
     */
    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    @Override
    public int compareTo(User o) {
        return this.getId().compareTo(o.getId());
    }
}
