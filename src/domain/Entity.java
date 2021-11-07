package domain;

import java.io.Serializable;

/**
 *
 * @param <ID> id type
 */
public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    /**
     * id
     */
    private ID id;

    /**
     *
     * @return the entity's id
     */
    public ID getId() {
        return id;
    }

    /**
     *
     * @param id -> must not be null
     */
    public void setId(ID id) {
        this.id = id;
    }
}