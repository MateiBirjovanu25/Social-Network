package com.example.map211psvm;

import com.example.map211psvm.controller.page.PageController;
import com.example.map211psvm.controller.request.RequestController;
import com.example.map211psvm.domain.validators.FriendshipValidator;
import com.example.map211psvm.domain.validators.UserValidator;
import com.example.map211psvm.repository.FriendshipRepository;
import com.example.map211psvm.repository.UserRepository;
import com.example.map211psvm.services.CommunityService;
import com.example.map211psvm.services.FriendshipService;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.services.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class MainForTesting extends Application {

    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("page/page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        SuperService superService = createSuperService();
        fxmlLoader.<PageController>getController().setSuperServiceAndUser(superService, superService.userService.findOne(3L).get());
        stage.setTitle("Cocaina");
        stage.setScene(scene);
        stage.show();
    }

    public static SuperService createSuperService() throws NoSuchAlgorithmException {
        var properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);
        } catch(IOException ignored) { ignored.printStackTrace(); }
        var userRepository = new UserRepository(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
        var friendshipRepository = new FriendshipRepository(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
        var userService = new UserService(userRepository, new UserValidator());
        var friendshipService = new FriendshipService(friendshipRepository, new FriendshipValidator());
        var communityService = new CommunityService(userRepository);
        return new SuperService(userService, friendshipService, communityService);
    }

    public static void main(String[] args) throws InterruptedException {
        launch();
    }
}
