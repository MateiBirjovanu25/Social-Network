package com.example.map211psvm;

import com.example.map211psvm.controller.AddRemoveController;
import com.example.map211psvm.controller.LoginController;
import com.example.map211psvm.controller.ChatController;
import com.example.map211psvm.controller.ReportController;
import com.example.map211psvm.controller.event.EventController;
import com.example.map211psvm.controller.notification.NotificationsListController;
import com.example.map211psvm.controller.page.PageController;
import com.example.map211psvm.controller.request.RequestController;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController {

    public Label labelName;
    @FXML
    private ToggleButton notificationsButton;
    @FXML
    private Pane pane;
    @FXML
    private Pane notificationsPane;
    private SuperService superService;
    private User user;


    @FXML
    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        homeButtonOnAction(new ActionEvent());
        labelName.setText(user.toString());
    }

    public void homeButtonOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("page/page.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<PageController>getController().setSuperServiceAndUser(superService, user);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {ignored.printStackTrace();}
    }

    public void friendsButtonOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add_remove.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<AddRemoveController>getController().setSuperService(superService);
            loader.<AddRemoveController>getController().setUser(user);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {
        }
    }

    public void friendRequestsButtonOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("request/requests.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<RequestController>getController().setSuperServiceAndUser(superService, user);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {
        }
    }

    public void eventsButtonOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("event/event.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<EventController>getController().setSuperServiceAndUser(superService, user);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {
        }
    }

    public void logOutButtonOnAction(ActionEvent actionEvent) {
        var loader = new FXMLLoader(Main.class.getResource("login.fxml"));
        try {
            var stage = (Stage) pane.getScene().getWindow();
            var scene = new Scene(loader.load());
            loader.<LoginController>getController().setSuperService(superService);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messagesButtonOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("chat/chat.fxml"));
        var stage = (Stage) pane.getScene().getWindow();
        var scene = new Scene(loader.load());
        loader.<ChatController>getController().setSuperService(superService);
        loader.<ChatController>getController().setUser(user);
        stage.setScene(scene);
    }

    public void notificationsButtonOnAction(ActionEvent actionEvent) {
        notificationsPane.setVisible(notificationsButton.isSelected());
        if(!notificationsButton.isSelected()) {
            notificationsPane.getChildren().clear();
            return;
        }
        var loader = new FXMLLoader(Main.class.getResource("notification/notificationsList.fxml"));
        try {
            var parent = (Parent) loader.load();
            loader.<NotificationsListController>getController().setSuperServiceAndUser(superService, user);
            notificationsPane.getChildren().add(parent);
        } catch (IOException ignored) {}
    }
}