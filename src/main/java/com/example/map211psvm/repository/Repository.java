package com.example.map211psvm.repository;

import com.example.map211psvm.domain.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {

    /** Finds an Entity by ID.
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id or null optional if wasn't found.
     */
    Optional<E> findOne(ID id);

    /** Finds all the Entities.
     * @return all entities
     */
    Iterable<E> findAll();

    /** Saves an Entity.
     * @param entity
     *         entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     *                             - the entity (id already exists)
     */
    Optional<E> save(E entity);

    /** Deletes an Entity.
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return an {@code Optional}
     *            - null if there is no entity with the given id,
     *            - the removed entity, otherwise
     */
    Optional<E> delete(ID id);

    /** Updates an Entity.
     * @param entity
     *          entity must not be null
     * @return  an {@code Optional}
     *             - previous entity if the entity was updated
     *             - null if the entity doesn't exist.
     */
    Optional<E> update(E entity);
}
