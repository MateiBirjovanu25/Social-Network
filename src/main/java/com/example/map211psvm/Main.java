package com.example.map211psvm;

import com.example.map211psvm.controller.LoginController;
import com.example.map211psvm.domain.validators.FriendshipValidator;
import com.example.map211psvm.domain.validators.MessageValidator;
import com.example.map211psvm.domain.validators.UserValidator;
import com.example.map211psvm.repository.EventRepository;
import com.example.map211psvm.repository.FriendshipRepository;
import com.example.map211psvm.repository.MessageRepository;
import com.example.map211psvm.repository.UserRepository;
import com.example.map211psvm.services.*;
import com.example.map211psvm.utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

public class Main extends Application {

    public void openAWindow(SuperService superService, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxmlLoader.<LoginController>getController().setSuperService(superService);
        stage.setTitle("Quokk.");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("img/happyQuokka.png")).toExternalForm()));
        stage.show();
    }

    public void openWithLogIn(SuperService superService, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxmlLoader.<MainController>getController().setSuperServiceAndUser(superService, superService.userService.findOne(3L).get());
        stage.setTitle("Quokk.");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {
        var superService = createSuperService();
        openAWindow(superService, stage);
        var stageTwo = new Stage();
        stageTwo.initModality(Modality.WINDOW_MODAL);
        openAWindow(superService, stageTwo);
//        openWithLogIn(createSuperService(), stage);
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

    public static void main(String[] args) {
        launch();
    }

}