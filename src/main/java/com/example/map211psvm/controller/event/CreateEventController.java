package com.example.map211psvm.controller.event;

import com.example.map211psvm.Main;
import com.example.map211psvm.controller.LoginController;
import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class CreateEventController {

    private SuperService superService;
    private User user;
    @FXML
    private TextField nameLabel;
    @FXML
    private TextField descriptionLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<LocalTime> timeSpinner;

    SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<>() {
        {
            setValue(defaultValue());
        }

        private LocalTime defaultValue() {
            return LocalTime.now().truncatedTo(ChronoUnit.HOURS);
        }

        @Override
        public void decrement(int steps) {
            LocalTime value = getValue();
            setValue(value == null ? defaultValue() : value.minusMinutes(15));
        }

        @Override
        public void increment(int steps) {
            LocalTime value = getValue();
            setValue(value == null ? defaultValue() : value.plusMinutes(15));
        }
    };

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        datePicker.setValue(LocalDate.now());
        timeSpinner.setValueFactory(factory);
        timeSpinner.setEditable(true);
    }

    @FXML
    public void createButtonOnAction(ActionEvent actionEvent) {
        LocalDateTime dateTime = datePicker.getValue().atTime(timeSpinner.getValue());
        var event = new Event(nameLabel.getText(), descriptionLabel.getText(), user, dateTime);
        superService.eventService.save(event);
        cancelButtonOnAction(new ActionEvent());
    }

    @FXML
    public void cancelButtonOnAction(ActionEvent actionEvent) {
        var stage = (Stage) descriptionLabel.getScene().getWindow();
        stage.close();
    }
}
