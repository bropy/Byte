package com.steam.service;


import com.steam.dto.UserDto;
import com.steam.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
