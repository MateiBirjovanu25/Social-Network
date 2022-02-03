package com.example.map211psvm.controller;

import com.example.map211psvm.Main;
import com.example.map211psvm.MainController;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.domain.validators.ValidationException;
import com.example.map211psvm.repository.RepositoryException;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.Hasher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private SuperService superService;
    @FXML
    private Button logInButton;
    @FXML
    private Label invalidCredentialsLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink hyperlink;

    public LoginController() {}
    public void setSuperService(SuperService superService){
        this.superService = superService;
    }

    public void logInButtonOnAction(ActionEvent event) {
        try {
            var user = superService.userService.findOnePassword(usernameField.getText(),superService.userService.hash(passwordField.getText()));
            if(user.isPresent()){
                var stage = (Stage) hyperlink.getScene().getWindow();
                var loader = new FXMLLoader(Main.class.getResource("main.fxml"));
                var scene = new Scene(loader.load());
                loader.<MainController>getController().setSuperServiceAndUser(superService, user.get());
                stage.setScene(scene);
                return;
            }
        } catch (ValidationException | RepositoryException | IOException | NoSuchAlgorithmException e) {
            invalidCredentialsLabel.setText(e.getMessage());
            return;
        }
        invalidCredentialsLabel.setText("This user doesn't exist.");
    }

    public void signUpHyperLinkOnAction(ActionEvent actionEvent) throws Exception {
        var stage = (Stage) hyperlink.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("sign_up.fxml"));
        var scene = new Scene(loader.load());
        String cssTextField = Main.class.getResource("css/text-field.css").toExternalForm();
        String cssButton = Main.class.getResource("css/button.css").toExternalForm();
        String cssCheckBox = Main.class.getResource("css/button.css").toExternalForm();
        String cssLabel = Main.class.getResource("css/label.css").toExternalForm();
        scene.getStylesheets().addAll(List.of(cssLabel, cssButton, cssCheckBox, cssTextField));
        loader.<SignUpController>getController().setSuperService(superService);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
