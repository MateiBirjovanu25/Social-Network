package repository.memory;

import domain.Entity;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @param <ID> id type
 * @param <E> entity type
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    /**
     * validator
     */
    private Validator<E> validator;
    /**
     * map of entities
     */
    Map<ID,E> entities;

    /**
     *
     * @param validator -> must not be null
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    /**
     *
     * @param id
     *         entity must be not null
     * @return returns the entity that has the specified id
     * @throws IllegalArgumentException
     *             if the given id is null.     *
     */
    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    /**
     * @return returns the list of entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null if the has been added successfully
     *         otherwise, return the entity
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        boolean entityExists = false;
        for(E ent: entities.values())
        {
            if (ent.equals(entity)) {
                entityExists = true;
                break;
            }
        }
        if(entityExists) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    /**
     *
     * @param id must not be null
     * @return returns the entity if it has been removed successfully
     *         otherwise, return null
     * @throws IllegalArgumentException
     *             if the given id is null.     *
     */
    @Override
    public E delete(ID id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");
        if(entities.get(id) == null)
        {
            return null;
        }
        return entities.remove(id);
    }

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null if the entity has been updated successfully
     *         otherwise, return the entity
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);


        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }



}
