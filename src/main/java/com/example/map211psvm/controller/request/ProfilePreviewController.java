package com.example.map211psvm.controller.request;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class ProfilePreviewController {

    @FXML
    AnchorPane root;
    @FXML
    Label nameLabel;
    @FXML
    Label emailLabel;
    @FXML
    Circle profileCircle;
    User user;

    public void setUser(User user){
        this.user = user;
        setProfile();
    }

    private void setProfile() {
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        emailLabel.setText(user.getEmail());
        var photoPath = user.getPhotoPath() == null ? Objects.requireNonNull(Main.class.getResource("img/squirrel.png")).toExternalForm() : user.getPhotoPath();
        profileCircle.setFill(new ImagePattern(new Image(photoPath)));
    }

    @FXML
    private void exitButtonOnAction(ActionEvent actionEvent) {
        root.setVisible(false);
    }
}
