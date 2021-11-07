package repository.file;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;


import java.io.*;

import java.util.Arrays;
import java.util.List;


/**
 *
 * @param <ID> id type
 * @param <E> entity type
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    /**
     * file's name
     */
    String fileName;

    /**
     *
     * @param fileName not null
     * @param validator not null
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * load data from file
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine())!=null )
            {
                if(!line.equals("")) {
                    List<String> attributes = Arrays.asList(line.split(";"));
                    E entity = extractEntity(attributes);
                    super.save(entity);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes split arguments from file
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);

    /**
     *
     * @param entity not null
     * @return string representing entity
     */
    protected abstract String createEntityAsString(E entity);

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
    public E save(E entity){
        if(super.save(entity) == null)
        {
            writeToFile(entity);
            return null;
        }
        else
            return entity;
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
    public E delete(ID id) {
        E removedEntity = super.delete(id);
        if(removedEntity == null)
        {
            return null;
        }
        writeToFile();
        return removedEntity;
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
        E e = super.update(entity);
        if (e == null)
        {
            writeToFile();
            return null;
        }
        return entity;
    }

    /**
     *
     * @param entity
     *         entity must be not null
     * appends to file the string representing the entity
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(createEntityAsString(entity));
            bw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * writes to file the string representing the entities
     */
    protected void writeToFile(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false))) {
            super.findAll().forEach(entity ->
            {
                try
                {
                    bw.write(createEntityAsString(entity));
                    bw.write('\n');
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

