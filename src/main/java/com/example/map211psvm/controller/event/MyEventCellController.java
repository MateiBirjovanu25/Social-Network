package com.example.map211psvm.controller.event;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MyEventCellController extends ListCell<Event> {

    @FXML
    Label nameLabel;
    @FXML
    Label descriptionLabel;
    @FXML
    Button deleteButton;
    @FXML
    Button editButton;
    @FXML
    AnchorPane pane;
    SuperService superService;
    User user;

    public MyEventCellController(SuperService superService, User user){
        this.superService = superService;
        this.user = user;
    }

    @Override
    protected void updateItem(Event eventItem, boolean empty){
        super.updateItem(eventItem, empty);
        if(eventItem == null){
            setText(null);
            setGraphic(null);
            return;
        }
        loadFxml();
        nameLabel.setText(eventItem.getName());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setText(eventItem.getDescription());
        editButton.setOnAction(event -> {
            var stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("event/editAnEvent.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load());
                fxmlLoader.<EditEventController>getController().setSuperServiceAndUser(superService, user, eventItem);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ignored) {ignored.printStackTrace();}
        });
        deleteButton.setOnAction(event -> {
            superService.eventService.delete(eventItem.getId());
        });
        setText(null);
        setGraphic(pane);
    }

    private void loadFxml(){
        var loader = new FXMLLoader(Main.class.getResource("event/myEventCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
