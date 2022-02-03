package com.example.map211psvm.domain.dtos;

import com.example.map211psvm.domain.Entity;

public class UserDto extends Entity<Long> {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;

    public UserDto(Long id, String first_name, String last_name, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    @Override
    public String toString() {
        return  "ID = " + id +
                "\nFirst name = " + first_name +
                "\nLast name = " + last_name +
                "\nEmail = " + email;
    }
}
