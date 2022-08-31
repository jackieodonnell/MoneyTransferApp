package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

public class UserDTO {

    @NotEmpty
    private int userId;
    @NotEmpty
    private String username;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
