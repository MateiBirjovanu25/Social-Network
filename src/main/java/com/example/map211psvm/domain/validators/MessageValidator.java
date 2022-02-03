package com.example.map211psvm.domain.validators;

import com.example.map211psvm.domain.Message;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        String errors = "";
        if(entity == null)
            throw new IllegalArgumentException("You can't have a null Message.");
        if(entity.getContent().equals(""))
            errors += "The content of the message is required";
        if(entity.getToUser().contains(entity.getFromUser()) || (entity.getReply() != null && entity.getFromUser().equals(entity.getReply().getFromUser())))
            errors += "Stop talking alone...";
        if(!errors.equals(""))
            throw new ValidationException(errors);
    }
}
