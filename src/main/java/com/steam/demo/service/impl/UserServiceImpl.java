package com.steam.demo.service.impl;

import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Role;
import com.steam.demo.entity.User;
import com.steam.demo.repository.RoleRepository;
import com.steam.demo.repository.UserRepository;
import com.steam.demo.service.UserService;
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
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());

        Profile profile = new Profile();
        profile.setNickname(userDto.getLogin());
        profile.setAvatar("https://t4.ftcdn.net/jpg/05/49/98/39/360_F_549983970_bRCkYfk0P6PP5fKbMhZMIb07mCJ6esXL.jpg");
        profile.setDescription("Опис");
        profile.setStatus("Status");
        profile.setCountry("Earth");
        profile.setUser(user);

        user.setProfile(profile);

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = createRoleIfNotFound("ROLE_USER");
        }
        user.setRoles(List.of(role));

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setLogin(user.getLogin());
                    userDto.setEmail(user.getEmail());
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    userDto.setBirthDate(user.getBirthDate());
                    return userDto;
                })
                .collect(Collectors.toList());
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
    public User createUser(User user) {
        // Ensure password is encoded before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLogin(userDetails.getLogin());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            user.setEmail(userDetails.getEmail());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setBirthDate(userDetails.getBirthDate());
            // Update profile if needed
            if (userDetails.getProfile() != null) {
                user.setProfile(userDetails.getProfile());
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public List<User> getUserFriends(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> List.copyOf(user.getFriends())).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id)
        );
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<User> optionalFriend = userRepository.findById(friendId);
        if (optionalUser.isPresent() && optionalFriend.isPresent()) {
            User user = optionalUser.get();
            User friend = optionalFriend.get();
            user.getFriends().add(friend);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User or friend not found.");
        }
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<User> optionalFriend = userRepository.findById(friendId);
        if (optionalUser.isPresent() && optionalFriend.isPresent()) {
            User user = optionalUser.get();
            User friend = optionalFriend.get();
            user.getFriends().remove(friend);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User or friend not found.");
        }
    }

    private Role createRoleIfNotFound(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
