package com.example.map211psvm.controller;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.domain.validators.ValidationException;
import com.example.map211psvm.repository.RepositoryException;
import com.example.map211psvm.services.SuperService;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AddRemoveController implements Initializable, PropertyChangeListener {
    public Button buttonBack;
    private SuperService superService;
    private User user;
    @FXML
    private Button buttonFriends;
    @FXML
    private Button buttonDelete;
    @FXML
    private TextField textSearch;
    @FXML
    private Button buttonGo;
    @FXML
    private Button buttonAdd;
    @FXML
    private ListView listFriends;

    public AddRemoveController (){}

    public void setSuperService(SuperService superService){
        this.superService = superService;
        this.superService.friendshipService.addPropertyChangeListener(this);
    }

    public void setUser(User user){
        User newUser = superService.userService.findOne(user.getId()).get();
        this.user = newUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonAdd.setVisible(false);
    }

    Predicate<User> predicateStartsWith(String string){
        return(x -> x.getLastName().startsWith(string) || x.getFirstName().startsWith(string));
    }

    List<User> getUsersStartingWith(String string){
       ArrayList<User> users = new ArrayList<>();
        superService.userService.findAll().forEach(users::add);
        return users
                .stream()
                .filter(predicateStartsWith(string))
                .toList();
    }


    public void buttonGoAction(ActionEvent actionEvent) {
        String nume = textSearch.getText();
        listFriends.setItems(FXCollections.observableList(getUsersStartingWith(nume)));
    }

    public void buttonActionDelete(ActionEvent actionEvent) {
        User deletedUser = (User) listFriends.getSelectionModel().getSelectedItem();
        superService.friendshipService.delete(user.getId(), deletedUser.getId());
        listFriends.getItems().removeAll(listFriends.getSelectionModel().getSelectedItem());
    }

    public void buttonFriendsAction(ActionEvent actionEvent) {
        listFriends.setItems(FXCollections.observableList(user.getFriendList()));
    }

    public void buttonAddAction(ActionEvent actionEvent) {
        User addedUser = (User)listFriends.getSelectionModel().getSelectedItem();
        superService.friendshipService.save(user.getId(),addedUser.getId());
    }

    public void buttonBackAction(ActionEvent actionEvent) throws IOException {
        var stage = (Stage) textSearch.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("user_screen.fxml"));
        var scene = new Scene(loader.load());
        loader.<UserScreenController>getController().setSuperService(superService);
        loader.<UserScreenController>getController().setUser(user);
        loader.<UserScreenController>getController().setName();
        stage.setScene(scene);
    }

    public void clickItem(MouseEvent mouseEvent) {
        User addedUser = (User)listFriends.getSelectionModel().getSelectedItem();
        if(superService.friendshipService.findOne(user.getId(), addedUser.getId()).isPresent() ){
            buttonAdd.setVisible(false);
            buttonDelete.setVisible(true);
        }
        else if(Objects.equals(addedUser.getId(), user.getId())){
            buttonAdd.setVisible(false);
            buttonDelete.setVisible(false);
        }
        else{
            buttonDelete.setVisible(false);
            buttonAdd.setVisible(true);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        superService.userService.findOne(user.getId()).ifPresent(x -> user = x);
        listFriends.setItems(FXCollections.observableList(user.getFriendList()));
    }
}
