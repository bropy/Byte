package com.steam.demo.service;


import com.steam.demo.dto.SafeUserDto;
import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByLogin(String login);

    UserDto createUser(User user);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    List<User> getUserFriends(Long id);

    void addFriend(Long id, Long friendId);

    void removeFriend(SafeUserDto id, Long friendId);
    UserDto authenticateUser(String login, String password);

    UserDto getUserDtoById(Long userId);

    Set<User> getUsersByIds(Set<SafeUserDto> developers);
}
