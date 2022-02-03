package com.example.map211psvm.controller.chat;

import com.example.map211psvm.domain.Message;
import com.example.map211psvm.services.MessageService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ListViewCell extends ListCell<Message> {
    private Long idUser;
    private MessageService messageService;
    public ListViewCell(Long userId,MessageService messageService) {
        this.idUser = userId;
        this.messageService = messageService;
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            VBox vBox = new VBox();
            vBox.getStylesheets().add("com/example/map211psvm/css/styleLabel.css");
            vBox.getStyleClass().add("vbox");
            if (item.getFromUser().getId() == idUser) { // the user is also the sender
                vBox.setAlignment(Pos.CENTER_RIGHT);
                Label label = styleLabel(item.getContent());
                label.setAlignment(Pos.CENTER_RIGHT);
                var reply=item.getReply();
                if(reply==null) //the message is not a reply
                    vBox.getChildren().addAll(label);
                else //the message is not a reply
                {
                    label =styleLabelReplyContent(item.getContent());
                    var user=reply.getFromUser();
                    Label textReply=styleReplyLabel(user.toString());
                    textReply.setAlignment(Pos.CENTER_RIGHT);
                    Label replyLabel=styleReplyLabel('"'+reply.getContent()+'"');
                    replyLabel.setAlignment(Pos.CENTER_RIGHT);
                    vBox.getChildren().addAll(textReply,replyLabel,label);
                }
                setGraphic(vBox);
            }
            else{ // the user is not the sender
                HBox hBox = new HBox();
                hBox.setSpacing(5);
                HBox pane = new HBox();
                pane.setAlignment(Pos.CENTER);
                pane.getStylesheets().add("com/example/map211psvm/css/styleLabel.css");
                pane.getStyleClass().add("hBox");
                ImageView imageView = new ImageView(item.getFromUser().getPhotoPath());
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                pane.getChildren().add(imageView);
                hBox.getChildren().add(pane);
                vBox.setAlignment(Pos.CENTER_LEFT);
                Label label = styleLabel(item.getContent());
                label.setAlignment(Pos.CENTER_LEFT);
                var reply=item.getReply();
                if(reply==null) { //the message is not a reply
                    hBox.getChildren().add(label);
                    vBox.getChildren().addAll(hBox);
                }
                else //the message is not a reply
                {
                    label =styleLabelReplyContent(item.getContent());
                    var user=reply.getFromUser();
                    Label textReply=styleReplyLabel(user.toString());
                    textReply.setAlignment(Pos.CENTER_LEFT);
                    Label replyLabel=styleReplyLabel('"'+reply.getContent()+'"');
                    replyLabel.setAlignment(Pos.CENTER_LEFT);
                    hBox.getChildren().add(label);
                    vBox.getChildren().addAll(textReply,replyLabel,hBox);
                }
                setGraphic(vBox);
            }
        }
    }

    private Label styleLabel(String msg){
        var label=new Label(msg);
        label.setMinWidth(50);
        label.setMaxHeight(20);
        label.getStyleClass().remove("label");
        label.getStylesheets().add("com/example/map211psvm/css/styleLabel.css");
        label.getStyleClass().add("styleLabel");
        return label;
    }

    private Label styleLabelReplyContent(String msg){
        var label=new Label(msg);
        label.setMinWidth(50);
        label.setMaxHeight(20);
        label.getStyleClass().remove("label");
        label.getStylesheets().add("com/example/map211psvm/css/styleLabel.css");
        label.getStyleClass().add("styleLabelReplyContent");
        return label;
    }

    private Label styleReplyLabel(String msg){
        var label=new Label(msg);
        label.setMinWidth(50);
        label.setMinHeight(30);
        label.getStyleClass().remove("label");
        label.getStylesheets().add("com/example/map211psvm/css/styleLabel.css");
        label.getStyleClass().add("styleLabelReply");
        return label;
    }
}