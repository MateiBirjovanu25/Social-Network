package com.example.map211psvm.controller.event;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.Event;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;


public class MyEventsController implements PropertyChangeListener{

    private SuperService superService;
    private User user;
    @FXML
    private ListView<Event> listView;
    @FXML
    private ObservableList<Event> model;
    @FXML
    private Label pageLabel;
    private int currentPage = 0;
    private int maxPage;
    private final int noOnPage = 5;
    @FXML
    private Pane eventPane;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        superService.eventService.addPropertyChangeListener(this);
        initModel();
    }

    public MyEventsController() {
        model = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        listView.setItems(model);
        listView.setCellFactory(x -> new MyEventCellController(superService, user));
    }

    public void initModel() {
        var noOfEvents = superService.eventService.noOfEventsMadeByAUser(user);
        maxPage = noOfEvents / this.noOnPage + (noOfEvents % this.noOnPage != 0 ? 1 : 0) - 1;
        loadPageInModel(this.currentPage);
        pageLabel.setText((currentPage + 1) + " / "  + (maxPage + 1));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        initModel();
    }

    private void loadPageInModel(int pageNumber) {
        var events = superService.eventService.someEventsMadeByAUser(user, pageNumber * noOnPage, noOnPage);
        model.setAll(events);
    }

    public void nextPageButtonOnAction(ActionEvent actionEvent) {
        if(this.currentPage != this.maxPage) {
            this.currentPage++;
            loadPageInModel(this.currentPage);
            pageLabel.setText((currentPage + 1) + " / " + (maxPage + 1));
        }
    }

    public void prevPageButtonOnAction(ActionEvent actionEvent) {
        if (this.currentPage != 0) {
            this.currentPage--;
            loadPageInModel(this.currentPage);
            pageLabel.setText((currentPage + 1) + " / " + (maxPage + 1));
        }
    }

    public void setEventPane(Pane eventPane) {
        this.eventPane = eventPane;
    }

    @FXML
    public void showDetails(MouseEvent mouseEvent) {
        eventPane.setVisible(false);
        if(mouseEvent.getClickCount() == 2){
            var event = listView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("event/eventPreview.fxml"));
            try {
                var parent = (Parent) loader.load();
                loader.<EventPreview>getController().setEvent(event);
                eventPane.getChildren().clear();
                eventPane.getChildren().add(parent);
                eventPane.setVisible(true);
            } catch (IOException ignored) {}
        }
    }
}
