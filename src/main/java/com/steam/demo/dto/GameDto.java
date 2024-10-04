package com.steam.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.steam.demo.dto.AchievementDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String avatar;
    private String source;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @NotNull(message = "Price is required")
    private Double price;

    private boolean approved;

    @NotBlank(message = "Developer login is required")
    private String developer;

    private List<AchievementDto> achievements;


    @NotBlank(message = "Game type is required")
    @JsonProperty("type_game")
    private String typeGame; // Type of the game (e.g., Action, RPG, etc.)

    @NotBlank(message = "Player count is required")
    private String players; // Number of players (e.g., Single-player, Multiplayer)

    @NotBlank(message = "Device support is required")
    private String deviceSupport; // Supported devices (e.g., PC, Console, Mobile)

    @NotBlank(message = "Status is required")
    private String status; // Current status of the game (e.g., Released, In Development)
}
