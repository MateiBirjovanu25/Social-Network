package com.example.map211psvm.controller.chat;

import com.example.map211psvm.domain.Message;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.MessageService;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.services.UserService;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class FriendListCell extends ListCell<User> {
    private Long idUser;
    private SuperService superService;

    public FriendListCell(Long userId,SuperService superService) {
        this.idUser = userId;
        this.superService = superService;
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            HBox hBox = new HBox();
            hBox.setSpacing(5);
            hBox.getStylesheets().add("com/example/map211psvm/css/friendList.css");
            hBox.getStyleClass().add("vbox");
            hBox.setAlignment(Pos.CENTER_LEFT);
            Label label = styleLabel(item.toString());
            HBox pane = new HBox();
            pane.getStylesheets().add("com/example/map211psvm/css/paneStyle.css");
            pane.getStyleClass().add("pane");
            ImageView imageView = new ImageView(item.getPhotoPath());
            imageView.setFitHeight(55);
            imageView.setFitWidth(55);
            pane.setAlignment(Pos.CENTER);
            pane.getChildren().add(imageView);
            hBox.getChildren().add(pane);
            hBox.getChildren().add(label);
            setGraphic(hBox);
        }
    }

    private Label styleLabel(String msg){
        var label=new Label(msg);
        label.getStyleClass().remove("label");
        label.getStylesheets().add("com/example/map211psvm/css/friendList.css");
        label.getStyleClass().add("friendLabelStyle");
        label.getStyleClass().add("outline");
        return label;
    }

}
