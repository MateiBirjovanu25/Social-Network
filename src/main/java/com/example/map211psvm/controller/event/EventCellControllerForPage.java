package com.example.map211psvm.controller.event;

import com.example.map211psvm.Main;
import com.example.map211psvm.controller.request.ProfilePreviewController;
import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class EventCellControllerForPage {

    @FXML
    Label nameLabel;
    @FXML
    Label descriptionLabel;
    @FXML
    Button participateButton;
    @FXML
    Button cancelButton;
    @FXML
    AnchorPane pane;
    @FXML
    ToggleButton notificationsButton;
    @FXML
    private Pane eventPane;
    SuperService superService;
    User user;
    Event event;

    public void setSuperServiceAndUser(SuperService superService, User user, Event event){
        this.superService = superService;
        this.user = user;
        this.event = event;
        initCell();
    }

    protected void initCell(){
        nameLabel.setText(this.event.getName());
        descriptionLabel.setText(this.event.getDescription());
        setButtons(this.event);
        participateButton.setOnAction(event -> {
            participateButton.setVisible(false);
            cancelButton.setVisible(true);
            superService.eventService.addUserToEvent(this.event.getId(), user.getId());
        });
        cancelButton.setOnAction(event -> {
            participateButton.setVisible(true);
            cancelButton.setVisible(false);
            superService.eventService.removeUserToEvent(this.event.getId(), user.getId());
        });
        notificationsButton.setOnAction(event -> {
            superService.eventService.setNotifications(user.getId(), this.event.getId(), notificationsButton.isSelected());
            if(Objects.equals(notificationsButton.getText(), "Follow")){
                notificationsButton.setText("Unfollow");
            }
            else
            {
                notificationsButton.setText("Follow");
            }
        });
        pane.setOnMouseClicked(mouseEvent -> {
            eventPane.setVisible(false);
            if(mouseEvent.getClickCount() == 2){
                eventPane.setVisible(true);
                var loaderProfile = new FXMLLoader(Main.class.getResource("event/eventPreview.fxml"));
                try {
                    var parent = (Parent) loaderProfile.load();
                    loaderProfile.<EventPreview>getController().setEvent(event);
                    eventPane.getChildren().clear();
                    eventPane.getChildren().add(parent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setButtons(Event eventItem){
        var notifications = eventItem.getParticipants().get(user);
        cancelButton.setVisible(notifications != null);
        notificationsButton.setVisible(notifications != null);
        participateButton.setVisible(notifications == null);
        notificationsButton.setSelected(notifications != null && notifications);
        notificationsButton.setText("Follow");
    }

    public void setEventPane(Pane eventPane) {
        this.eventPane = eventPane;
    }
}
