package com.example.map211psvm.controller.event;

import com.example.map211psvm.Main;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class EventsPageController implements PropertyChangeListener {

    private User user;
    private SuperService superService;
    @FXML
    private Pagination pagination;
    private final int numberOnPage = 5;
    private Pane eventPane;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        initModel();
        this.superService.eventService.addPropertyChangeListener(this);
    }

    private void initModel() {
        var noOfEvents = superService.eventService.noOfEventsNotMadeByAUser(user);
        pagination.setPageCount(noOfEvents / this.numberOnPage + (noOfEvents % this.numberOnPage != 0 ? 1 : 0));
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox vbox = new VBox();
        var list = superService.eventService.someEventsNotMadeByAUser(user, pageIndex * this.numberOnPage, this.numberOnPage);
        list.forEach( item -> {
            var loader = new FXMLLoader(Main.class.getResource("event/eventCellForPage.fxml"));
            try {
                Parent parent = (Parent) loader.load();
                loader.<EventCellControllerForPage>getController().setSuperServiceAndUser(superService, user, item);
                loader.<EventCellControllerForPage>getController().setEventPane(eventPane);
                 vbox.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return vbox;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        var index = pagination.getCurrentPageIndex();
        initModel();
        pagination.setCurrentPageIndex(index);
    }

    public void setEventPane(Pane eventPane) {
        this.eventPane = eventPane;
        pagination.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() != 2)
                this.eventPane.setVisible(false);
        });
    }
}
