package com.example.map211psvm.controller.notification;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.*;
import com.example.map211psvm.domain.enums.Notifications;
import com.example.map211psvm.services.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NotificationCellController extends ListCell<Notification> {

    @FXML
    private AnchorPane pane;
    @FXML
    AnchorPane root;
    @FXML
    Label titleLabel;
    @FXML
    Label descriptionLabel;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    ImageView imageView;

    SuperService superService;
    User user;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
    }

    public void setRoot(AnchorPane root) {
        this.root = root;
    }

    @Override
    protected void updateItem(Notification notification, boolean empty) {
        if (notification == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        var loader = new FXMLLoader(Main.class.getResource("notification/notificationCell.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        titleLabel.setWrapText(true);
        if(notification.getType().equals(Notifications.Message)) {
            Image image = new Image(Main.class.getResource("/com/example/map211psvm/img/mesaj.png").toExternalForm());
            imageView.setImage(image);
            var message = (Message) notification.getEntity();
            titleLabel.setText("New Message from " + message.getFromUser().getFirstName());
        }
        if(notification.getType().equals(Notifications.Event)) {
            Image image = new Image(Main.class.getResource("/com/example/map211psvm/img/event.png").toExternalForm());
            imageView.setImage(image);
            titleLabel.setText("Event coming up");
        }
        if(notification.getType().equals(Notifications.FriendRequest)) {
            Image image = new Image(Main.class.getResource("/com/example/map211psvm/img/add-user.png").toExternalForm());
            imageView.setImage(image);
            var user = (Friendship) notification.getEntity();
            titleLabel.setText("New Request from " + user.getId().getFirst().getFirstName());
        }
        descriptionLabel.setText(notification.getDescription());
        setText(null);
        setGraphic(pane);
    }
}
