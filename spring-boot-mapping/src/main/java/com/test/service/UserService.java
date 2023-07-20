package com.test.service;

import com.test.model.UserModel;

public interface UserService {

    UserModel getUserByName(String username);
    UserModel saveUserDetails(UserModel userModel);
}
