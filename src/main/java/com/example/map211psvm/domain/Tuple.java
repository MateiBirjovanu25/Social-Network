package com.example.map211psvm.domain;

import java.util.Objects;

public class Tuple<E extends Comparable<E>>{
    private E first;
    private E second;

    /** Class constructor.
     *
     * @param first the first element of the tuple.
     * @param second thr second element of the tuple.
     */
    public Tuple(E first, E second) {
        this.first = first;
        this.second = second;
    }

    /** Gets the first element of the tuple.
     *
     * @return first element of the tuple.
     */
    public E getFirst() {
        return first;
    }

    /** Sets the first element of the tuple.
     *
     * @param first the new first element.
     */
    public void setFirst(E first) {
        this.first = first;
    }

    /** Gets the second element of the tuple.
     *
     * @return the second element of the tuple.
     */
    public E getSecond() {
        return second;
    }

    /** Sets the second element of the tuple.
     *
     * @param second the new second element of the tuple.
     */
    public void setSecond(E second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        Tuple<?> tuple = (Tuple<?>) o;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second) ||
        Objects.equals(first, tuple.second) && Objects.equals(second, tuple.first);
    }

    @Override
    public int hashCode() {
        return first.compareTo(second) > 0 ? Objects.hash(first, second) : Objects.hash(second, first);
    }
}
