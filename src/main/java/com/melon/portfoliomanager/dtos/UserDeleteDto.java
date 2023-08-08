package com.melon.portfoliomanager.dtos;

import jakarta.validation.constraints.NotBlank;

public class UserDeleteDto {

    @NotBlank(message = "The specified username can't be blank when requesting to delete a user.")
    private String username;

    public UserDeleteDto(String username) {
        this.username = username;
    }

    public UserDeleteDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
