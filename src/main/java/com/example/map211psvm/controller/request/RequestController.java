package com.example.map211psvm.controller.request;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class RequestController {

    @FXML
    private Pane pane;
    @FXML
    private Pane profilePane;
    @FXML
    private ListView<User> searchList;
    @FXML
    private ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    private TextField searchBar;
    @FXML
    private Button changeButton;
    private SuperService superService;
    private User user;
    private Boolean requestsReceived;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        changeToYourRequestsButton(new ActionEvent());
        searchBar.textProperty().addListener(x -> {
            profilePane.setVisible(false);
            if(searchBar.getText().equals(""))
                searchList.setVisible(false);
            else {
                searchList.setVisible(true);
                initModel(searchBar.getText());
            }
        });
    }

    public void initModel(String name) {
        model.setAll(superService.userService.findAllStartWith(name, user));
    }

    @FXML
    public void initialize() {
        requestsReceived = false;
        searchList.setItems(model);
        searchList.setCellFactory(x -> {
            var cell = new SearchCellController();
            cell.setSuperServiceAndUser(superService, user);
            cell.setProfilePane(profilePane);
            return cell;
        });
    }

    public void changePane(ActionEvent actionEvent) {
        searchBar.setText("");
        profilePane.setVisible(false);
        if(requestsReceived) {
            changeButton.setText("Requests send.");
            changeToYourRequestsButton(actionEvent);
        }
        else {
            changeButton.setText("Requests received.");
            changeToSendByYouRequestsButton(actionEvent);
        }
        requestsReceived = !requestsReceived;
    }

    public void changeToYourRequestsButton(ActionEvent actionEvent) {
        var loader = new FXMLLoader(Main.class.getResource("request/receivedRequestsPane.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<ReceivedRequestController>getController().setSuperServiceAndUser(superService, user);
            loader.<ReceivedRequestController>getController().setProfilePaneAndSearchBar(profilePane, searchBar);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {}
    }

    public void changeToSendByYouRequestsButton(ActionEvent actionEvent) {
        var loader = new FXMLLoader(Main.class.getResource("request/sendRequestsPane.fxml"));
        try {
            Parent parent = (Parent) loader.load();
            loader.<SendRequestController>getController().setSuperServiceAndUser(superService, user);
            loader.<SendRequestController>getController().setProfilePaneAndSearchBar(profilePane, searchBar);
            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException ignored) {}
    }

    @FXML
    public void onMouseClickedOnAction(MouseEvent mouseEvent) {
        profilePane.setVisible(false);
        searchBar.setText("");
    }

    @FXML
    public void searchBarOnMouseClicked(MouseEvent mouseEvent) {
        profilePane.setVisible(false);
    }
}
