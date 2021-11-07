package domain;

import java.util.Objects;

/**
 *
 * @param <E1> -> first element's type
 * @param <E2> -> second element's type
 */
public class Tuple<E1,E2> {
    /**
     * first element
     */
    private E1 e1;
    /**
     * second element
     */
    private E2 e2;

    /**
     *
     * @param e1 -> must not be null
     * @param e2 -> must not be null
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     *
     * @return the first element
     */
    public E1 getE1() {
        return e1;
    }

    /**
     *
     * @return the second element
     */
    public E2 getE2() {
        return e2;
    }

    /**
     *
     * @param o the other element
     * @return comparison result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(e1, tuple.e1) && Objects.equals(e2, tuple.e2);
    }

    /**
     *
     * @return hash of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
