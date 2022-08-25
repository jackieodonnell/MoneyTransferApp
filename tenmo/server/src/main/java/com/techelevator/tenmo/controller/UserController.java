package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserDTO> findAllUsers() {
        List<UserDTO> userList = userDao.findAll();
        return userList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username)  {
        User user = userDao.findByUsername(username);
        return user;
    }

/*    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public int getIdByUser(@PathVariable String username) {
        int id = userDao.findIdByUsername(username);
        return id;
    }*/
}
