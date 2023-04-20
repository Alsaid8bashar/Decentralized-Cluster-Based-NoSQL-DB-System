package org.example.Authentication;

import org.example.Model.User;

public class UserManager {
    public User registration(String username, String password) {
        long nextId=User.getTheNextId();
        User user = new User(TokenService.tokenGenerator(username, password,nextId),nextId);
        return user;
    }
}
