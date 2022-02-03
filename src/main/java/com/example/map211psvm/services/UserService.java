package com.example.map211psvm.services;

import com.example.map211psvm.domain.User;
import com.example.map211psvm.domain.validators.Validator;
import com.example.map211psvm.repository.UserRepository;
import com.example.map211psvm.utils.Hasher;
import com.example.map211psvm.utils.RSAEncryption;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class UserService {
    UserRepository repository;
    Validator<User> validator;
    Hasher hasher;
    RSAEncryption encryption;

    public UserService(UserRepository repository, Validator<User> validator) throws NoSuchAlgorithmException {
        this.repository = repository;
        this.validator = validator;
        this.hasher = new Hasher();
        this.encryption = new RSAEncryption();
    }

    public String hash(String password) throws NoSuchAlgorithmException {
        return hasher.hash(password);
    }

    public Optional<User> findOne(Long id) {
        return repository.findOne(id);
    }

    public Optional<User> findOne(String email){
        return repository.findOne(email);
    }

    public Optional<User> findOnePassword(String email, String password){
        Optional<User> user =  repository.findOne(email);
        if(user.isEmpty())
            return user;
        if(user.get().getPassword().equals(password))
            return user;
        return Optional.empty();
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }

    private void createKeyFile(User user, String key) throws IOException {
        FileWriter fileWriter = new FileWriter("src/main/resources/keys/"+user.getEmail()+".txt");
        fileWriter.write(key);
        fileWriter.close();
    }

    public Optional<User> save(String firstName, String lastName, String email, String password, String publicKey, String photoPath) throws NoSuchAlgorithmException, IOException {
        var user = new User(firstName, lastName, email, password, publicKey, photoPath);
        validator.validate(user);
        encryption.generateKeys();
        user.setPublicKey(encryption.getPrivateKey());
        Optional<User> savedUser = repository.save(user);
        if(savedUser.isEmpty()){
            createKeyFile(user, encryption.getPublicKey());
        }
        return savedUser;
    }

    public Optional<User> delete(Long id){
        return repository.delete(id);
    }

    public Optional<User> update(Long id, String firstName, String lastName, String email, String password) {
        var user = new User(id, firstName, lastName, email, password);
        validator.validate(user);
        return repository.update(user);
    }

    public List<User> findAllStartWith(String name, User user) {
        return repository.findAllStartWith(name, user);
    }

    public String readUserKey(User user) throws IOException {
        String key = Files.readString(Path.of("src/main/resources/keys/" + user.getEmail() + ".txt"));
        return key;
    }
}
