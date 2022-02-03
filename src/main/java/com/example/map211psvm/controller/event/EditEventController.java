package com.example.map211psvm.controller.event;

import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class EditEventController {

    private SuperService superService;
    private User user;
    private Event event;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<LocalTime> timeSpinner;

    SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<>() {
        {
            setValue(defaultValue());
        }

        private LocalTime defaultValue() {
            return LocalTime.now();
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

    public void setSuperServiceAndUser(SuperService superService, User user, Event event) {
        this.superService = superService;
        this.user = user;
        this.event = event;
        nameTextField.setText(event.getName());
        descriptionTextField.setText(event.getDescription());
        datePicker.setValue(event.getDateTime().toLocalDate());
        factory.setValue(event.getDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES));
        timeSpinner.setValueFactory(factory);
        timeSpinner.setEditable(true);
    }

    @FXML
    public void editButtonOnAction(ActionEvent actionEvent) {
        LocalDateTime dateTime = datePicker.getValue().atTime(timeSpinner.getValue());
        var event = new Event(this.event.getId(), nameTextField.getText(), descriptionTextField.getText(), user, dateTime);
        superService.eventService.update(event);
        cancelButtonOnAction(new ActionEvent());
    }

    @FXML
    public void cancelButtonOnAction(ActionEvent actionEvent) {
        var stage = (Stage) descriptionTextField.getScene().getWindow();
        stage.close();
    }
}
