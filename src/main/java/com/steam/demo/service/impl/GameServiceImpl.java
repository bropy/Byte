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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Override
    public Optional<GameDto> getGameById(Long id) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        return gameOptional.map(this::convertToDto);
    }

    @Override
    public List<UserGameDto> getGamesByUserId(Long userId) {
        List<UserGames> userGamesList = userGamesRepository.findByUserId(userId);
        return userGamesList.stream()
                .map(this::convertToUserGameDto)
                .collect(Collectors.toList());
    }

    @Override
    public GameDto createGame(GameDto gameDto) {
        // Конвертуємо GameDto в Game entity
        Game game = new Game();
        game.setTitle(gameDto.getTitle());
        game.setDescription(gameDto.getDescription());
        game.setSource(gameDto.getSource());
        game.setReleaseDate(gameDto.getReleaseDate());
        game.setPrice(gameDto.getPrice());
        game.setApproved(gameDto.isApproved());

        // Перевіряємо, чи є розробник в DTO, і отримуємо його з бази даних за ідентифікатором
        if (gameDto.getDeveloper() != null) {
            Optional<User> developerOptional = userRepository.findById(gameDto.getDeveloper());
            if (developerOptional.isPresent()) {
                game.setDeveloper(developerOptional.get()); // Встановлюємо знайденого розробника
            } else {
                throw new IllegalArgumentException("Developer not found with id: " + gameDto.getDeveloper());
            }
        }

        // Зберігаємо гру
        Game savedGame = gameRepository.save(game);

        // Повертаємо збережену гру як DTO
        return convertToDto(savedGame);
    }

    private GameDto convertToDto(Game game) {
        // Get the developer's ID, or null if there's no developer
        Long developerId = game.getDeveloper() != null ? game.getDeveloper().getId() : null;

        // Побудова GameDto з відповідними полями
        return GameDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .avatar(game.getAvatar())  // Assuming Game entity has an avatar field
                .source(game.getSource())
                .releaseDate(game.getReleaseDate())
                .price(game.getPrice())
                .approved(game.isApproved())
                .developer(developerId)
                .build();
    }
    private UserGameDto convertToUserGameDto(UserGames userGames) {
        Game game = userGames.getGame();
        Set<AchievementDto> achievementDtos = userGames.getAchievements().stream()
                .map(this::convertAchievementToDto)
                .collect(Collectors.toSet());

        return UserGameDto.builder()
                .gameId(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .playtimeHours(userGames.getPlaytimeHours())
                .lastPlayed(userGames.getLastPlayed())
                .status(userGames.getStatus())
                .avatar(userGames.getAvatar())
                .total(userGames.getTotal())
                .achievements(achievementDtos)
                .build();
    }

    private AchievementDto convertAchievementToDto(Achievement achievement) {
        return AchievementDto.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .instruction(achievement.getInstruction())
                .image(achievement.getImage())
                .build();
    }

    // Конвертація SafeUserDto в User
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

    // Конвертація User в SafeUserDto
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
}
