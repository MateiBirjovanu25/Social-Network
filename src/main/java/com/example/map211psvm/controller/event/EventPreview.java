package com.example.map211psvm.controller.event;

import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.utils.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Map;

public class EventPreview {

    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ListView<User> listView;
    @FXML
    private Label organizerLabel;
    @FXML
    private ObservableList<User> model;
    @FXML
    private Event event;

    public EventPreview() {
        model = FXCollections.observableArrayList();
    }

    public void setEvent(Event event) {
        this.event = event;
        listView.setItems(model);
        model.setAll(this.event.getParticipants().keySet().stream().toList());
        nameLabel.setText(event.getName());
        descriptionLabel.setText(event.getDescription());
        organizerLabel.setText("Organized by " + event.getOrganizer().getFirstName() + event.getOrganizer().getLastName());
        timeLabel.setText("Date: " + event.getDateTime().format(Constants.DATE_TIME_FORMATTER));
    }
}
