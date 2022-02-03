package com.example.map211psvm.controller.page;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.RSAEncryption;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class FriendCellController extends ListCell<User> {

    @FXML
    Label nameLabel;
    @FXML
    Button chatButton;
    @FXML
    Button cancelButton;
    @FXML
    Button sendButton;
    @FXML
    TextField textField;
    @FXML
    AnchorPane pane;
    @FXML
    AnchorPane detailsPane;
    @FXML
    AnchorPane messagePane;
    SuperService superService;
    User user;
    RSAEncryption encryption;
    @FXML
    private Button deleteButton;

    public void setSuperServiceAndUser(SuperService superService, User user) throws NoSuchAlgorithmException {
        this.superService = superService;
        this.user = user;
        this.encryption = new RSAEncryption();
    }

    @Override
    protected void updateItem(User friend, boolean empty){
        super.updateItem(friend, empty);
        if(friend == null){
            setText(null);
            setGraphic(null);
            return;
        }
        var loader = new FXMLLoader(Main.class.getResource("page/friendCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        nameLabel.setText(friend.getFirstName() + " " + friend.getLastName());
        chatButton.setOnAction(x -> {
            messagePane.setVisible(true);
            detailsPane.setVisible(false);
        });
        cancelButton.setOnAction(x -> {
            textField.clear();
            messagePane.setVisible(false);
            detailsPane.setVisible(true);
        });
        deleteButton.setOnAction(x -> superService.friendshipService.delete(user.getId(), friend.getId()));
        sendButton.setOnAction(x ->{
            String encodedText="";
            try {
                System.out.println(friend);
                encodedText = encryption.encode(textField.getText(),friend.getPublicKey());
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            superService.messageService.save(user.getId(), List.of(friend.getId()), encodedText);
            textField.clear();
            messagePane.setVisible(false);
            detailsPane.setVisible(true);
        });
        setText(null);
        setGraphic(pane);
    }
}
