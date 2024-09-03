package com.steam.demo.service.impl;

import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Role;
import com.steam.demo.entity.User;
import com.steam.demo.repository.RoleRepository;
import com.steam.demo.repository.UserRepository;
import com.steam.demo.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Profile profile = user.getProfile();
        if (profile != null) {
            profile.setUser(user);
        }

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = createRoleIfNotFound("ROLE_USER");
        }
        user.setRoles(List.of(role));

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public void saveUser(UserDto userDto) {

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login));
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setLogin(userDetails.getLogin());
                    user.setEmail(userDetails.getEmail());
                    user.setFirstName(userDetails.getFirstName());
                    user.setLastName(userDetails.getLastName());
                    user.setBirthDate(userDetails.getBirthDate());
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    if (userDetails.getProfile() != null) {
                        user.getProfile().setNickname(userDetails.getProfile().getNickname());
                        user.getProfile().setAvatar(userDetails.getProfile().getAvatar());
                        user.getProfile().setDescription(userDetails.getProfile().getDescription());
                        user.getProfile().setCountry(userDetails.getProfile().getCountry());
                        user.getProfile().setStatus(userDetails.getProfile().getStatus());
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        return getUserById(id)
                .map(user -> List.copyOf(user.getFriends()))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        User friend = getUserById(friendId).orElseThrow(() -> new RuntimeException("Friend not found with id: " + friendId));
        user.getFriends().add(friend);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFriend(Long id, Long friendId) {
        User user = getUserById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        User friend = getUserById(friendId).orElseThrow(() -> new RuntimeException("Friend not found with id: " + friendId));
        user.getFriends().remove(friend);
        userRepository.save(user);
    }

    private Role createRoleIfNotFound(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setBirthDate(user.getBirthDate());
        return userDto;
    }
}