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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class ReceivedRequestCellController extends ListCell<User> {

    @FXML
    Label nameLabel;
    @FXML
    Button accButton;
    @FXML
    Button rejButton;
    @FXML
    AnchorPane pane;
    @FXML
    Circle profileCircle;
    @FXML
    private Pane profilePane;
    @FXML
    private TextField searchBar;
    SuperService superService;
    User user;

    public void setSuperServiceAndUser(SuperService superService, User user){
        this.superService = superService;
        this.user = user;
    }

    @Override
    protected void updateItem(User friend, boolean empty){
        super.updateItem(friend, empty);
        if(friend == null){
            setText(null);
            setGraphic(null);
            return;
        }
        var loader = new FXMLLoader(Main.class.getResource("request/requestReceivedCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        nameLabel.setText(friend.getFirstName() + " " + friend.getLastName());
        var photoPath = friend.getPhotoPath() == null ? Objects.requireNonNull(Main.class.getResource("img/squirrel.png")).toExternalForm() : friend.getPhotoPath();
        profileCircle.setFill(new ImagePattern(new Image(photoPath)));
        accButton.setOnAction(event -> superService.friendshipService.update(friend.getId(), user.getId(), "approved"));
        rejButton.setOnAction(event -> superService.friendshipService.update(friend.getId(), user.getId(), "declined"));
        this.setOnMouseClicked(mouseEvent -> {
            searchBar.setText("");
            profilePane.setVisible(false);
            if(mouseEvent.getClickCount() == 2) {
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
        });
        setText(null);
        setGraphic(pane);
    }

    public void setProfilePaneAndSearchBar(Pane profilePane, TextField searchBar) {
        this.profilePane = profilePane;
        this.searchBar = searchBar;
    }
}
