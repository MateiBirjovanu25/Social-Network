package domain;

import java.util.List;
import java.util.Objects;

/**
 * user entity
 */
public class User extends Entity<Long>{
    /**
     * first name of the user
     */
    private String firstName;
    /**
     * last name of the user
     */
    private String lastName;
    /**
     * friend list of the user
     * */
    private List<User> friends;

    public User() {
    }

    /**
     *
     * @param firstName not null
     * @param lastName not null
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     *
     * @return first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName not null
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName not null
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return friends list
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     *
     * @param friends friends list
     */
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    /**
     *
     * @return -> the string representing the user
     */
    @Override
    public String toString() {
        return super.getId()+" "+firstName+" "+lastName;
    }

    /**
     *
     * @param o -> the other user
     * @return -> the result of the comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getId().equals(that.getId());
    }

    /**
     *
     * @return -> hash of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}