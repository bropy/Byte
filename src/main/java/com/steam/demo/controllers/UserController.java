package com.steam.demo.controllers;

import com.steam.demo.dto.SafeUserDto;
import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.User;
import com.steam.demo.service.ProfileService;
import com.steam.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SafeUserDto> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> {
            SafeUserDto safeUserDto = mapToSafeUserDto(user);
            return ResponseEntity.ok(safeUserDto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private SafeUserDto mapToSafeUserDto(User user) {
        SafeUserDto safeUserDto = new SafeUserDto();
        BeanUtils.copyProperties(user, safeUserDto, "friends");
        if (user.getFriends() != null) {
            safeUserDto.setFriends(user.getFriends().stream()
                    .map(friend -> {
                        SafeUserDto friendDto = new SafeUserDto();
                        BeanUtils.copyProperties(friend, friendDto, "friends");
                        return friendDto;
                    })
                    .collect(Collectors.toSet()));
        }
        return safeUserDto;
    }

    @GetMapping("/search/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        Optional<User> user = userService.getUserByLogin(login);
        return user.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }
    @GetMapping("/friendsOf/{id}")
    public ResponseEntity<List<Profile>> getFriendsProfilesByUserId(@PathVariable Long id) {
        Optional<User> user = profileService.getUserById(id);
        if (user.isPresent()) {
            Set<User> friends = user.get().getFriends();
            List<Profile> friendsProfiles = new ArrayList<>();
            for (User friend : friends) {
                Optional<Profile> friendProfile = profileService.getProfileByUserId(friend.getId());
                if (friendProfile.isPresent()) {
                    friendsProfiles.add(friendProfile.get());
                }
            }
            return ResponseEntity.ok(friendsProfiles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getUserFriends(@PathVariable Long id) {
        List<User> friends = userService.getUserFriends(id);
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable SafeUserDto id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        return ResponseEntity.noContent().build();
    }
}
