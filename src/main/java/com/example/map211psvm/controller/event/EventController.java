package com.example.map211psvm.controller.event;

import com.example.map211psvm.Main;
import com.example.map211psvm.controller.LoginController;
import com.example.map211psvm.controller.request.ReceivedRequestController;
import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.attribute.AttributeView;


public class EventController {

    private SuperService superService;
    private User user;
    @FXML
    private Label label;
    @FXML
    private Pane pane;
    @FXML
    private Button changeEventsButton;
    @FXML
    private Pane eventPane;
    private Boolean myEvents;


    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        eventsOnAction(new ActionEvent());
        changeEventsButton.setText("All events.");
        myEvents = false;
    }

    public void changeEvents(ActionEvent actionEvent) {
        myEvents = !myEvents;
        if(!myEvents) {
            eventsOnAction(new ActionEvent());
            changeEventsButton.setText("All events.");
        }
        else {
            myEventsOnAction(new ActionEvent());
            changeEventsButton.setText("My events.");
        }
    }

    @FXML
    public void eventsOnAction(ActionEvent actionEvent) {
        var loader = new FXMLLoader(Main.class.getResource("event/eventsPage.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<EventsPageController>getController().setSuperServiceAndUser(superService, user);
            loader.<EventsPageController>getController().setEventPane(eventPane);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void myEventsOnAction(ActionEvent actionEvent) {
        var loader = new FXMLLoader(Main.class.getResource("event/myEventsPane.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<MyEventsController>getController().setSuperServiceAndUser(superService, user);
            loader.<MyEventsController>getController().setEventPane(eventPane);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {}
    }

    @FXML
    public void createAnEvent(ActionEvent actionEvent) {
        var stage = new Stage();
        eventPane.setVisible(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("event/createAnEvent.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            fxmlLoader.<CreateEventController>getController().setSuperServiceAndUser(superService, user);
            stage.setTitle("ReFaInSn");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ignored) {ignored.printStackTrace();}
    }
}
