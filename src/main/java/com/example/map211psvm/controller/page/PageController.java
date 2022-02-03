package com.example.map211psvm.controller.page;

import com.example.map211psvm.Main;
import com.example.map211psvm.controller.ReportController;
import com.example.map211psvm.controller.event.EventController;
import com.example.map211psvm.controller.request.RequestController;
import com.example.map211psvm.domain.Page;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class PageController implements PropertyChangeListener {

    @FXML
    AnchorPane pageRoot;
    @FXML
    ProgressBar loadingBar;
    @FXML
    Label nameLabel;
    @FXML
    ImageView image;
    @FXML
    Label listLabel;
    @FXML
    Label noOfFriendsLabel;
    @FXML
    Label noOfEventsLabel;
    @FXML
    Label emailLabel;
    @FXML
    Label eventsParticipatedLabel;
    @FXML
    ListView<User> list;
    @FXML
    ObservableList<User> model;
    Page page;
    SuperService superService;
    User user;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.page = superService.createPage(user);
        this.user = user;
        drawProfile();
        initModel();
        superService.addPropertyChangeListener(this);
    }

    @FXML
    public void initialize() {
        list.setItems(model);
        list.setCellFactory(x -> {
            var cell = new FriendCellController();
            try {
                cell.setSuperServiceAndUser(superService, user);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return cell;
        });
    }

    private void drawProfile() {
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        emailLabel.setText(user.getEmail());
        noOfFriendsLabel.setText(page.getFriendsList().size() + " friends.");
        var events = page.getEventList().stream().filter(p -> p.getOrganizer().equals(user)).toList().size();
        noOfEventsLabel.setText(events + " events organized.");
        events = page.getEventList().stream().filter(p -> p.getParticipants().containsKey(user)).toList().size();
        eventsParticipatedLabel.setText(events + " events you attended.");
        Image image2 = new Image(user.getPhotoPath());
        image.setImage(image2);
    }

    @FXML
    public void goToRequests(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("request/requests.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<RequestController>getController().setSuperServiceAndUser(superService, user);
            pageRoot.getChildren().clear();
            pageRoot.getChildren().add(parent);
        } catch (IOException ignored) {}
    }

    @FXML
    public void goToEvents(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("event/event.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<EventController>getController().setSuperServiceAndUser(superService, user);
            pageRoot.getChildren().clear();
            pageRoot.getChildren().add(parent);
        } catch (IOException ignored) {}
    }

    private void initModel() {
        model.clear();
        model.addAll(page.getFriendsList());
    }

    public void reportButtonOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("report.fxml"));
        var scene = new Scene(loader.load());
        loader.<ReportController>getController().setSuperServiceAndUser(superService, user);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public PageController() {
        model = FXCollections.observableArrayList();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        drawProfile();
        initModel();
    }
}