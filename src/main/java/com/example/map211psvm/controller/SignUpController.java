package com.example.map211psvm.controller;
import com.example.map211psvm.Main;
import com.example.map211psvm.MainController;
import com.example.map211psvm.domain.validators.ValidationException;
import com.example.map211psvm.repository.RepositoryException;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.RSAEncryption;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class SignUpController {

    private SuperService superService;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button backButton;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Label firstNameErrorLabel;
    @FXML
    private Label lastNameErrorLabel ;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private CheckBox checkBox;
    private RSAEncryption encryption;
    @FXML
    private Circle circlePhoto;
    private String filePath = null;

    public void createAccount(ActionEvent actionEvent){
        clearLabels();
        if(!checkBox.isSelected())
            passwordErrorLabel.setText("You must accept terms and conditions");
        try{
            var userOptional = superService.userService.save(firstNameField.getText(), lastNameField.getText(), emailField.getText(), superService.userService.hash(passwordField.getText()),null, filePath);
            userOptional.ifPresentOrElse(x -> passwordErrorLabel.setText("You already have an account."), this::switchWindows);
        }catch (RepositoryException | ValidationException | NoSuchAlgorithmException | IOException e){
            setErrors(e.getMessage());
        }
    }

    private void switchWindows() {
        var user = superService.userService.findOne(emailField.getText());
        if (user.isPresent()) {
            try {
                var stage = (Stage) lastNameErrorLabel.getScene().getWindow();
                var loader = new FXMLLoader(Main.class.getResource("main.fxml"));
                var scene = new Scene(loader.load());
                loader.<MainController>getController().setSuperServiceAndUser(superService, user.get());
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setErrors(String message) {
        var errors = message.split("\n");
        Arrays.stream(errors).forEach( err ->
                {
                    if(err.contains("first name"))
                        firstNameErrorLabel.setText(err);
                    if(err.contains("last name"))
                        lastNameErrorLabel.setText(err);
                    if(err.contains("email"))
                        emailErrorLabel.setText(err);
                }
        );
    }

    private void clearLabels() {
        passwordErrorLabel.setText("");
        emailErrorLabel.setText("");
        firstNameErrorLabel.setText("");
        lastNameErrorLabel.setText("");
    }

    public void backToLogIn(ActionEvent actionEvent) throws IOException {
        var stage = (Stage) backButton.getScene().getWindow();
        var loader = new FXMLLoader(Main.class.getResource("login.fxml"));
        var scene = new Scene(loader.load());
        loader.<LoginController>getController().setSuperService(superService);
        stage.setScene(scene);
    }

    public void setSuperService(SuperService superService){
        this.superService = superService;
    }

    @FXML
    public void initialize(){
        circlePhoto.setFill(new ImagePattern(new Image(Objects.requireNonNull(Main.class.getResource("img/squirrel.png")).toExternalForm())));
    }

    @FXML
    public void addPhotoOnAction(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            circlePhoto.setFill(new ImagePattern(new Image(file.getAbsolutePath())));
            filePath = file.getAbsolutePath();
        }
    }
}
