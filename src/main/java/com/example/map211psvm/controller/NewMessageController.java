package com.example.map211psvm.controller;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NewMessageController {

    public TextField textField;
    public ListView<User> listView;
    public Button buttonNext;
    User user;
    SuperService superService;
    List<User> selectedUsers;
    String content;

    public void setSuperServiceAndUser(SuperService superService, User user){
        this.superService = superService;
        this.user = user;
        listView.setItems(FXCollections.observableList(user.getFriendList()));
        selectedUsers = new ArrayList<>();
        buttonNext.setVisible(false);
    }

    public void listViewClick(MouseEvent mouseEvent) {
        User toUser = listView.getSelectionModel().getSelectedItem();
        String newText="";
        if(selectedUsers.contains(toUser)){
            if(selectedUsers.size() == 1)
                selectedUsers = new ArrayList<>();
            else
                selectedUsers.remove(toUser);
        }
        else {
            selectedUsers.add(toUser);
        }
        for(User us : selectedUsers){
            newText+=us.toString()+" ";
        }
        if(selectedUsers.size()!=0)
            buttonNext.setVisible(true);
        else
            buttonNext.setVisible(false);
        content = newText;
        textField.setText(newText);
    }

    public void buttonNextOnAction(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("chat/message_window.fxml"));
        var stage = (Stage) textField.getScene().getWindow();
        var scene = new Scene(loader.load());
        loader.<MessageWindowController>getController().setSuperServiceAndUser(superService,user,selectedUsers,content);
        stage.setScene(scene);
    }

    public void buttonClearOnAction(ActionEvent actionEvent) {
        content = "";
        selectedUsers = new ArrayList<>();
        textField.setText("");
    }
}
