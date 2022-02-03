package com.example.map211psvm.controller;

import com.example.map211psvm.Main;
import com.example.map211psvm.MainController;
import com.example.map211psvm.controller.chat.FriendListCell;
import com.example.map211psvm.controller.chat.ListViewCell;
import com.example.map211psvm.controller.request.ProfilePreviewController;
import com.example.map211psvm.domain.Message;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.MessageService;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.RSAEncryption;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.function.Predicate;

public class ChatController implements PropertyChangeListener{
    @FXML
    public TextField textFieldFriend;
    public CheckBox checkBoxEncryption;
    public ImageView profileImage;
    public Button buttonImage;
    private SuperService superService;
    private User user;
    @FXML
    private ListView<User> friendsListView;
    @FXML
    private TextField friendsTextField;
    @FXML
    private ImageView buttonSearch;
    @FXML
    private TextField messageTextField;
    @FXML
    private ImageView buttonSend;
    @FXML
    private ListView<Message> messagesListView;
    RSAEncryption encryption = new RSAEncryption();

    public ChatController () throws NoSuchAlgorithmException {}


    public void setSuperService(SuperService superService){
        this.superService = superService;
        superService.messageService.addPropertyChangeListener(this);
        buttonImage.setVisible(false);
    }

    public void setUser(User user){
        messagesListView.setCellFactory(stringListView -> new ListViewCell(user.getId(),superService.messageService));
        friendsListView.setCellFactory(stringListView -> new FriendListCell(user.getId(),superService));
        User newUser = superService.userService.findOne(user.getId()).get();
        this.user = newUser;
        friendsListView.setItems(FXCollections.observableList(getUsersStartingWith("")));
    }

    Predicate<User> predicateStartsWith(String string){
        return(x -> (x.getLastName().startsWith(string) || x.getFirstName().startsWith(string)) && !x.getId().equals(user.getId()));
    }

    List<User> getUsersStartingWith(String string){
        ArrayList<User> users = new ArrayList<>();
        user.getFriendList().forEach(users::add);
        return users
                .stream()
                .filter(predicateStartsWith(string))
                .toList();
    }

    public void searchFriends(ActionEvent mouseEvent) {
        String nume = friendsTextField.getText();
        friendsListView.setItems(FXCollections.observableList(getUsersStartingWith(nume)));
    }

    public void buttonSendOnAction(ActionEvent actionEvent) throws NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        User toUser = friendsListView.getSelectionModel().getSelectedItem();
        String messageText = messageTextField.getText();
        messageTextField.setText("");
        String encodedText = encryption.encode(messageText,toUser.getPublicKey());
        if(messagesListView.getSelectionModel().getSelectedItem() == null)
            superService.messageService.save(user.getId(), List.of(toUser.getId()),encodedText);
        else{
            Message replyMessage = messagesListView.getSelectionModel().getSelectedItem();
            superService.messageService.saveReply(user.getId(),encodedText,replyMessage.getId());
        }
        messagesListView.getSelectionModel().clearSelection();
    }

    public List<Message> getDecodedConversation(List<Message> conversation) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String key = superService.userService.readUserKey(user);
        List<Message> decodedConversation = new ArrayList<>();
        for(Message message : conversation){
            Message decodedMessage = message;
            String decodedText="";


            if(Objects.equals(message.getFromUser().getId(), user.getId())) {
                String key2 = superService.userService.readUserKey(message.getToUser().get(0));
                if (message.getReply() != null) {
                    Message decodedMessageReply = message.getReply();
                    String decodedTextReply = encryption.decode(message.getReply().getContent(),key);
                    decodedMessageReply.setContent(decodedTextReply);
                    message.setReply(decodedMessageReply);
                }
                decodedText = encryption.decode(message.getContent(), key2);
            }
            else{
                String key2 = superService.userService.readUserKey(message.getFromUser());
                if (message.getReply() != null) {
                    Message decodedMessageReply = message.getReply();
                    String decodedTextReply = encryption.decode(message.getReply().getContent(),key2);
                    decodedMessageReply.setContent(decodedTextReply);
                    message.setReply(decodedMessageReply);
                }
                decodedText = encryption.decode(message.getContent(),key);
            }
            decodedMessage.setContent(decodedText);
            decodedConversation.add(decodedMessage);
        }
        return decodedConversation;
    }

    public void listViewFriendsOnAction(MouseEvent mouseEvent) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        User toUser = friendsListView.getSelectionModel().getSelectedItem();
        List<Message> conversation = superService.messageService.showAConversationBetweenTwoUsers(user.getId(),toUser.getId());
        for(Message message : conversation){
            User user = message.getFromUser();
            user.setPhotoPath(superService.userService.findOne(user.getId()).get().getPhotoPath());
            message.setFromUser(user);
        }
        List<Message> decodedConversation = getDecodedConversation(conversation);
        messagesListView.setItems(FXCollections.observableList(decodedConversation));
        textFieldFriend.setText(toUser.toString());
        File file = new File(toUser.getPhotoPath());
        Image image = new Image(file.toURI().toString());
        profileImage.setImage(image);
        buttonImage.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(friendsListView.getSelectionModel().getSelectedItem() != null){
            List<Message> conversation = superService.messageService.showAConversationBetweenTwoUsers(user.getId(),friendsListView.getSelectionModel().getSelectedItem().getId());
            List<Message> decodedConversation = new ArrayList<>();
            for(Message message : conversation){
                User user = message.getFromUser();
                user.setPhotoPath(superService.userService.findOne(user.getId()).get().getPhotoPath());
                message.setFromUser(user);
            }
            try {
                decodedConversation = getDecodedConversation(conversation);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            messagesListView.setItems(FXCollections.observableList(decodedConversation));
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        var stage = (Stage) textFieldFriend.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        var scene = new Scene(loader.load());
        loader.<MainController>getController().setSuperServiceAndUser(superService,user);
        stage.setScene(scene);
    }

    public void newMessageOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("chat/new_message.fxml"));
        var scene = new Scene(loader.load());
        loader.<NewMessageController>getController().setSuperServiceAndUser(superService, user);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void buttonImageOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("request/profilePreview.fxml"));
        var scene = new Scene(loader.load());
        loader.<ProfilePreviewController>getController().setUser(friendsListView.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
