package com.example.map211psvm.domain.validators;

import com.example.map211psvm.domain.User;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException, IllegalArgumentException {
        String errorMessage = "";
        if(entity == null)
            throw new IllegalArgumentException("You can't have a null User.");
        if(entity.getFirstName() == null)
            errorMessage += "You can't have a null first name.";
        if(entity.getLastName() == null)
            errorMessage += "\nYou can't have a null last name.";
        if(entity.getEmail() == null)
            errorMessage += "\nYou can't have a null email.";
        if(errorMessage.length() > 0)
            throw new IllegalArgumentException(errorMessage);
        errorMessage = "";
        if(!entity.getFirstName().matches(("[a-zA-Z]+")))
            errorMessage += "Invalid first name.";
        if(!entity.getLastName().matches(("[a-zA-Z]+")))
            errorMessage += "\nInvalid last name.";
        if(!entity.getEmail().matches("[a-zA-Z1-9._-]+@[a-zA-Z._1-9-]+"))
            errorMessage += "\nInvalid email.";
        if(errorMessage.length() > 0)
            throw new ValidationException(errorMessage);
    }
}
