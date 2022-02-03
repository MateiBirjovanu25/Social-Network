package com.example.map211psvm.controller;

import com.example.map211psvm.domain.Message;
import com.example.map211psvm.domain.User;
import com.example.map211psvm.services.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportController {
    public ChoiceBox<User> choiceBoxFriends;
    public DatePicker datePickerFrom;
    public DatePicker datePickerTo;
    private SuperService superService;
    private User user;

    public void setSuperServiceAndUser(SuperService superService, User user) {
        this.superService = superService;
        this.user = user;
        choiceBoxFriends.setItems(FXCollections.observableList(user.getFriendList()));
    }

    public void buttonReport2(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        List<Message> messages =  superService.messagesReceivedByaUser(user.getId(),choiceBoxFriends.getSelectionModel().getSelectedItem().getId());

        PDDocument document  = new PDDocument();
        document.addPage(new PDPage());
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 700);
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 25 );
        contentStream.showText("Messages from: "+choiceBoxFriends.getSelectionModel().getSelectedItem().toString());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 16 );
        for(Message message : messages){
            contentStream.showText(message.toString());
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();
        document.save("src/main/resources/reports/report2.pdf");
        document.close();
    }

    public void buttonReport1(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        LocalDate dateFrom = datePickerFrom.getValue();
        LocalDate dateTo = datePickerTo.getValue();
        List<User> friends = superService.newFriends(dateFrom,dateTo,user);
        List<Message> messages = superService.newMessages(dateFrom,dateTo,user);
        PDDocument document  = new PDDocument();
        document.addPage(new PDPage());
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 700);
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 25 );
        contentStream.showText("New friends: ");
        contentStream.newLine();
        contentStream.newLine();

        contentStream.setFont( PDType1Font.TIMES_ROMAN, 16 );
        for(User friend : friends){
            contentStream.showText(friend.toString());
            contentStream.newLine();
        }
        contentStream.newLine();
        contentStream.newLine();
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 25 );
        contentStream.showText("Messages received: ");
        contentStream.newLine();
        contentStream.newLine();
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 16 );
        for(Message message :messages){
            contentStream.showText(message.toString());
            contentStream.newLine();
        }

        contentStream.endText();
        contentStream.close();
        document.save("src/main/resources/reports/report1.pdf");
        document.close();

    }
}
