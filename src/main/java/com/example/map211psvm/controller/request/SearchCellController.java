package com.example.map211psvm.controller.request;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class SearchCellController extends ListCell<User> {
    @FXML
    Label nameLabel;
    @FXML
    Button sendButton;
    @FXML
    AnchorPane pane;
    @FXML
    ImageView friendsImage;
    @FXML
    ImageView pendingImage;
    @FXML
    Circle profileCircle;
    @FXML
    private Pane profilePane;

    SuperService superService;
    User user;
    ReceivedRequestController requestNewController;

    public void setSuperServiceAndUser(SuperService superService, User user){
        this.superService = superService;
        this.user = user;
    }

    @Override
    protected void updateItem(User friend, boolean empty){
        super.updateItem(friend, empty);
        if(friend == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        var loader = new FXMLLoader(Main.class.getResource("request/searchCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        var imagePath = friend.getPhotoPath() == null ? Objects.requireNonNull(Main.class.getResource("img/squirrel.png")).toExternalForm() : friend.getPhotoPath();
        profileCircle.setFill(new ImagePattern(new Image(imagePath)));
        nameLabel.setText(friend.getFirstName() + " " + friend.getLastName());
        if(superService.friendshipService.requestBetweenTwoUsers(user, friend))
            pendingImage.setVisible(true);
        else if(user.getFriendList().contains(friend))
            friendsImage.setVisible(true);
        else {
            sendButton.setOnAction(event -> {
                System.out.println("AAAA");
                superService.friendshipService.save(user.getId(), friend.getId());
            });
            sendButton.setVisible(true);
        }
        this.setOnMouseClicked(x -> {
            if(x.getClickCount() == 2) {
                profilePane.setVisible(true);
                var loaderProfile = new FXMLLoader(Main.class.getResource("request/profilePreview.fxml"));
                try {
                    var parent = (Parent) loaderProfile.load();
                    loaderProfile.<ProfilePreviewController>getController().setUser(friend);
                    profilePane.getChildren().clear();
                    profilePane.getChildren().add(parent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(x.getClickCount() == 1)
                profilePane.setVisible(false);
        });
        setText(null);
        setGraphic(pane);
    }

    public void setProfilePane(Pane profilePane) {
        this.profilePane = profilePane;
    }
}
