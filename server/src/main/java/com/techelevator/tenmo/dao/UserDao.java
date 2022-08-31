package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDTO;

import java.util.List;

public interface UserDao {

    List<UserDTO> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
