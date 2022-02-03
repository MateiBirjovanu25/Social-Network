package com.example.map211psvm.domain.validators;

import com.example.map211psvm.domain.Friendship;

import java.util.List;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException, IllegalArgumentException {
        String errors = "";
        if(entity == null)
            throw new IllegalArgumentException("You can't have a null Friendship.");
        if(entity.getId() == null)
            errors += "You can't have a null id.";
        if(entity.getId() != null && entity.getId().getFirst() == null)
            errors += "\nFirst id can't be null.";
        if(entity.getId() != null && entity.getId().getSecond() == null)
            errors += "\nSecond id can't be null.";
        if(entity.getDate() == null)
            errors += "\nYou can't have a null date.";
        if(!errors.equals(""))
            throw new IllegalArgumentException(errors);
        errors = "";
        if(entity.getId().getFirst().equals(entity.getId().getSecond()))
            errors += "You can't be friend with yourself.";
        var possibleStatus = List.of("approved", "rejected", "pending", "declined");
        if(!possibleStatus.contains(entity.getStatus()))
            errors += "\nThat's not a valid status.";
        if(!errors.equals(""))
            throw new ValidationException(errors);
    }
}
