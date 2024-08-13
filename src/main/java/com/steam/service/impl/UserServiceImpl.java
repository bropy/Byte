package com.steam.service.impl;

import com.steam.dto.UserDto;
import com.steam.entity.Profile;
import com.steam.entity.Role;
import com.steam.entity.User;
import com.steam.repository.RoleRepository;
import com.steam.repository.UserRepository;
import com.steam.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private Role createRoleIfNotFound(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
