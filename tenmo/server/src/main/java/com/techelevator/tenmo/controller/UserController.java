package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> findAllUsers() {
        List<User> userList = userDao.findAll();
        return userList;
    }

    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

/*    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public int getIdByUser(@PathVariable String username) {
        int id = userDao.findIdByUsername(username);
        return id;
    }*/
}
