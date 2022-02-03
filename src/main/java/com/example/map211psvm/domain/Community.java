package com.example.map211psvm.domain;

import java.util.List;

public class Community {
    List<User> userList;

    /** Class constructor.
     *
     * @param userList a list of users from this community.
     */
    public Community(List<User> userList) {
        this.userList = userList;
    }

    /** Gets the community users.
     *
     * @return list of all the users from this community.
     */
    public List<User> getUserList() {
        return userList;
    }

    /** Sets the user list.
     *
     * @param userList the new user list.
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
