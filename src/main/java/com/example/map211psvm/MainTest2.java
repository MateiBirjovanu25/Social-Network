package com.example.map211psvm;

import com.example.map211psvm.controller.event.EventController;
import com.example.map211psvm.controller.page.PageController;
import com.example.map211psvm.domain.validators.FriendshipValidator;
import com.example.map211psvm.domain.validators.MessageValidator;
import com.example.map211psvm.domain.validators.UserValidator;
import com.example.map211psvm.domain.validators.ValidationException;
import com.example.map211psvm.repository.*;
import com.example.map211psvm.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Properties;

public class MainTest2 extends Application {

    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {
        SuperService superService = createSuperService();
        stage.setTitle("Cocaina");
        startMenu(superService, stage);
    }

    public void startMenu(SuperService superService, Stage stage) {
        try {
            var user = superService.userService.findOne("serhio_antico@yahoo.com");
            if(user.isPresent()){
                var loader = new FXMLLoader(Main.class.getResource("main.fxml"));
                var scene = new Scene(loader.load());
                loader.<MainController>getController().setSuperServiceAndUser(superService, user.get());
                stage.setScene(scene);
                stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("img/happyQuokka.png")).toExternalForm()));
                stage.show();
            }
        } catch (ValidationException | RepositoryException | IOException ignored) {}
    }

    public static SuperService createSuperService() throws NoSuchAlgorithmException {
        var properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);
        } catch(IOException ignored) { ignored.printStackTrace(); }
        var messageRepository = new MessageRepository(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
        var userRepository = new UserRepository(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
        var friendshipRepository = new FriendshipRepository(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
        var userService = new UserService(userRepository, new UserValidator());
        var friendshipService = new FriendshipService(friendshipRepository, new FriendshipValidator());
        var communityService = new CommunityService(userRepository);
        var eventRepository = new EventRepository(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
        var eventService = new EventService(eventRepository);
        var messageService = new MessageService(messageRepository,new MessageValidator());
        return new SuperService(userService, friendshipService, communityService, eventService, messageService);
    }

    public static void main(String[] args) throws InterruptedException {
        launch();
    }
}
