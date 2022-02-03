package com.example.map211psvm.controller.request;

import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SendRequestController implements PropertyChangeListener {

    private SuperService superService;
    private User user;
    private ObservableList<User> model;
    @FXML
    private ListView<User> listView;
    @FXML
    private Pane profilePane;
    @FXML
    private TextField searchBar;

    public SendRequestController() {
        model = FXCollections.observableArrayList();
    }

    public void setSuperServiceAndUser(SuperService superService, User user){
        this.superService = superService;
        superService.friendshipService.addPropertyChangeListener(this);
        this.user = user;
        initModel();
    }

    @FXML
    public void initialize() {
        listView.setItems(model);
        listView.setCellFactory(x -> {
            var cell = new SendRequestCellController();
            cell.setSuperServiceAndUser(superService, user);
            cell.setProfilePaneAndSearchBar(profilePane, searchBar);
            return cell;
        });
    }

    public void initModel() {
        var requests = superService.friendshipService.findAllRequestsSendByAUser(user);
        model.setAll(requests);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        initModel();
    }

    public void setProfilePaneAndSearchBar(Pane profilePane, TextField searchBar) {
        this.profilePane = profilePane;
        this.searchBar = searchBar;
    }
}
