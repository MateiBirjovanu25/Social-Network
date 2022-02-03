package com.example.map211psvm.controller.notification;

import com.example.map211psvm.domain.Notification;
import com.example.map211psvm.domain.Page;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.domain.enums.Notifications;
import com.example.map211psvm.services.SuperService;
import com.example.map211psvm.utils.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class NotificationsListController {

    @FXML
    private ListView<Notification> listView;
    @FXML
    private AnchorPane root;
    @FXML
    ObservableList<Notification> model;

    Page page;
    SuperService superService;
    User user;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.page = superService.createPage(user);
        this.user = user;
        initModel();
    }

    @FXML
    public void initialize() {
        listView.setItems(model);
        listView.setCellFactory(x -> {
            var a = new NotificationCellController();
            a.setSuperServiceAndUser(superService, user);
            //a.setRoot(pageRoot);
            return a;
        });
    }


    private void initModel() {
        model.addAll(page
                .getEventList()
                .stream()
                .filter(event -> event.getParticipants().containsKey(user) && event.getParticipants().get(user))
                .map( event -> new Notification("Event coming up", event.getName() + " is coming.", Notifications.Event, event.getDateTime().toLocalDate(), event))
                .toList());
        model.addAll(page
                .getRequestsList()
                .stream()
                .map( us -> new Notification(us.getId().getFirst().getFirstName() + " send you a friend request", "Request sent on " + us.getDate().format(Constants.DATE_TIME_FORMATTER), Notifications.FriendRequest, us.getDate(), us))
                .toList());
        model.addAll(page
                .getMessagesList()
                .stream()
                .map( mes -> new Notification(mes.getFromUser().getFirstName() + " send you a message.", mes.getContent(), Notifications.Message, LocalDateTime.parse(mes.getDate()).toLocalDate(), mes))
                .toList());
        var list = model.stream().sorted(Comparator.comparing(Notification::getDateTime)).toList();
        model.clear();
        model.addAll(list);
        listView.setItems(model);
    }

    public NotificationsListController() {
        model = FXCollections.observableArrayList();
    }
}
