package domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * friendship entity
 */
public class Friendship extends Entity<Tuple<Long,Long>>{
    /**
     * first user of the friendship
     */
    private Long friendId1;
    /**
     * second user of the friendship
     */
    private Long friendId2;

    /**
     *
     * @param friendId1 not null, positive
     * @param friendId2 not null, positive
     */
    private String date;

    public Friendship() {
    }

    public Friendship(Long friendId1, Long friendId2) {
        this.friendId1 = friendId1;
        this.friendId2 = friendId2;
        setId(new Tuple(friendId1,friendId2));
        date = LocalDate.now().toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return first user's id
     */
    public Long getFriendId1() {
        return friendId1;
    }

    /**
     *
     * @return second user's id
     */
    public Long getFriendId2() {
        return friendId2;
    }

    /**
     *
     * @param friendId1 not null
     */
    public void setFriendId1(Long friendId1) {
        this.friendId1 = friendId1;
    }

    /**
     *
     * @param friendId2 not null
     */
    public void setFriendId2(Long friendId2) {
        this.friendId2 = friendId2;
    }

    @Override
    public String toString() {
        return friendId1+" "+friendId2+" "+date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return ((Objects.equals(friendId1, that.friendId1) && Objects.equals(friendId2, that.friendId2))
                || (Objects.equals(friendId1, that.friendId2) && Objects.equals(friendId2, that.friendId1)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId1, friendId2);
    }
}
