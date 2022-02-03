package com.example.map211psvm.controller;

import com.example.map211psvm.Main;
import com.example.map211psvm.controller.event.EventController;
import com.example.map211psvm.controller.request.RequestController;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserScreenController {
    SuperService superService;
    User user;

    public Button buttonBack;
    public Label labelName;
    public Button buttonRequests;
    public Button buttonFriends;

    public void setName(){
        labelName.setText(user.getFirstName()+" "+user.getLastName());
    }

    public void setSuperService(SuperService superService){
        this.superService = superService;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void buttonFriendsAction(ActionEvent actionEvent) throws IOException {
        var stage = (Stage) labelName.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("add_remove.fxml"));
        var scene = new Scene(loader.load());
        loader.<AddRemoveController>getController().setSuperService(superService);
        loader.<AddRemoveController>getController().setUser(user);
        stage.setScene(scene);
    }

    public void buttonRequestsAction(ActionEvent actionEvent) throws IOException {
        var stage = (Stage) labelName.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("request/requests.fxml"));
        String cssTextField = Main.class.getResource("css/text-field.css").toExternalForm();
        String cssButton = Main.class.getResource("css/button.css").toExternalForm();
        //String cssCheckBox = Main.class.getResource("css/button.css").toExternalForm();
        String cssTable = Main.class.getResource("css/table.css").toExternalForm();
        //String cssLabel = Main.class.getResource("css/label.css").toExternalForm();
        var scene = new Scene(loader.load());
        scene.getStylesheets().addAll(List.of(cssButton, cssTable, cssTextField));
        loader.<RequestController>getController().setSuperServiceAndUser(superService, user);
        stage.setScene(scene);
    }

    public void buttonBackAction(ActionEvent actionEvent) throws IOException{
        var stage = (Stage) labelName.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("login.fxml"));
        var scene = new Scene(loader.load());
        loader.<LoginController>getController().setSuperService(superService);
        stage.setScene(scene);
    }

    public void eventsButton(ActionEvent actionEvent) throws IOException {
        var stage = (Stage) labelName.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("event/event.fxml"));
        String cssTextField = Main.class.getResource("css/text-field.css").toExternalForm();
        String cssButton = Main.class.getResource("css/button.css").toExternalForm();
        //String cssCheckBox = Main.class.getResource("css/button.css").toExternalForm();
        String cssTable = Main.class.getResource("css/table.css").toExternalForm();
        //String cssLabel = Main.class.getResource("css/label.css").toExternalForm();
        var scene = new Scene(loader.load());
        scene.getStylesheets().addAll(List.of(cssButton, cssTable, cssTextField));
        loader.<EventController>getController().setSuperServiceAndUser(superService, user);
        stage.setScene(scene);
    }
}
