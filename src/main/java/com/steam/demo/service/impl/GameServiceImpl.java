package com.steam.demo.service.impl;

import com.steam.demo.dto.AchievementDto;
import com.steam.demo.dto.GameDto;
import com.steam.demo.dto.SafeUserDto;
import com.steam.demo.dto.UserGameDto;
import com.steam.demo.entity.Achievement;
import com.steam.demo.entity.Game;
import com.steam.demo.entity.User;
import com.steam.demo.entity.UserGames;
import com.steam.demo.repository.AchievementRepository;
import com.steam.demo.repository.GameRepository;
import com.steam.demo.repository.UserGamesRepository;
import com.steam.demo.repository.UserRepository;
import com.steam.demo.service.GameService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserGamesRepository userGamesRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Game> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Page<Game> searchGames(String query, Pageable pageable) {
        return gameRepository.findByTitleContainingIgnoreCase(query, pageable);
    }

    @Transactional
    public GameDto createGame(GameDto gameDto) {
        // Create a new Game entity
        Game game = new Game();
        game.setTitle(gameDto.getTitle());
        game.setDescription(gameDto.getDescription());
        game.setAvatar(gameDto.getAvatar());
        game.setSource(gameDto.getSource());
        game.setReleaseDate(gameDto.getReleaseDate().atStartOfDay());
        game.setPrice(gameDto.getPrice());
        game.setApproved(gameDto.isApproved());

        // Find the developer (User) by ID
        User developer = userRepository.findById(gameDto.getDeveloper())
                .orElseThrow(() -> new IllegalArgumentException("Developer not found with id: " + gameDto.getDeveloper()));
        game.setDeveloper(developer);

        // Save the game entity
        Game savedGame = gameRepository.save(game);

        // If achievements exist in the GameDto, create and save them
        if (gameDto.getAchievements() != null && !gameDto.getAchievements().isEmpty()) {
            List<Achievement> achievements = gameDto.getAchievements().stream()
                    .map(achievementDto -> createAchievement(achievementDto, savedGame))
                    .collect(Collectors.toList());
            achievementRepository.saveAll(achievements);
            savedGame.setAchievements(achievements);
        }

        // Return the saved game as a GameDto
        return convertToDto(savedGame);
    }

    // Create a new achievement for a game
    private Achievement createAchievement(AchievementDto achievementDto, Game game) {
        Achievement achievement = new Achievement();
        achievement.setName(achievementDto.getName());
        achievement.setInstruction(achievementDto.getInstruction());
        achievement.setImage(achievementDto.getImage());
        achievement.setGame(game);
        return achievement;
    }

    // Convert Game entity to GameDto
    private GameDto convertToDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .avatar(game.getAvatar())
                .source(game.getSource())
                .releaseDate(LocalDate.from(game.getReleaseDate()))
                .price(game.getPrice())
                .approved(game.isApproved())
                .developer(game.getDeveloper().getId())
                .achievements(game.getAchievements().stream()
                        .map(this::convertAchievementToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    // Convert Achievement entity to AchievementDto
    private AchievementDto convertAchievementToDto(Achievement achievement) {
        return AchievementDto.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .instruction(achievement.getInstruction())
                .image(achievement.getImage())
                .build();
    }

    @Override
    public Optional<GameDto> getGameById(Long id) {
        return gameRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<UserGameDto> getGamesByUserId(Long userId) {
        List<UserGames> userGamesList = userGamesRepository.findByUserId(userId);
        return userGamesList.stream()
                .map(this::convertToUserGameDto)
                .collect(Collectors.toList());
    }

    // Convert SafeUserDto to User
    private User convertSafeDtoToUser(SafeUserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());
        return user;
    }

    // Convert User entity to SafeUserDto
    private SafeUserDto convertUserToSafeDto(User user) {
        if (user == null) {
            return null;
        }

        return SafeUserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .build();
    }

    // Convert UserGames entity to UserGameDto
    private UserGameDto convertToUserGameDto(UserGames userGames) {
        return UserGameDto.builder()
                .gameId(userGames.getGame().getId())
                .userId(userGames.getUser().getId())
                .status(userGames.getStatus())
                .playtimeHours(userGames.getPlaytimeHours())
                .lastPlayed(userGames.getLastPlayed())
                .build();
    }
}
