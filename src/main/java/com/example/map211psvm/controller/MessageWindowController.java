package com.example.map211psvm.controller;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.Message;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.RSAEncryption;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class MessageWindowController {
    public TextField textFieldFriends;
    public TextField textFieldContent;
    private SuperService superService;
    private User user;
    List<User> toUsers;
    RSAEncryption encryption;


    public void setSuperServiceAndUser(SuperService superService, User user, List<User> toUsers, String content) throws NoSuchAlgorithmException {
        this.superService = superService;
        this.user = user;
        this.toUsers = toUsers;
        encryption = new RSAEncryption();
        textFieldFriends.setText(content);
    }

    public void buttonBackOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("chat/new_message.fxml"));
        var stage = (Stage) textFieldFriends.getScene().getWindow();
        var scene = new Scene(loader.load());
        loader.<NewMessageController>getController().setSuperServiceAndUser(superService,user);
        stage.setScene(scene);
    }

    public void buttonSendOnAction(ActionEvent actionEvent) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        try{
            for(User user2 : toUsers){
                String encodedText = encryption.encode(textFieldContent.getText(),user2.getPublicKey());
                superService.messageService.save(user.getId(),List.of(user2.getId()),encodedText);
            }
        }
        catch (RuntimeException e){}
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("chat/sent.fxml"));
        var stage = (Stage) textFieldFriends.getScene().getWindow();
        var scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}
