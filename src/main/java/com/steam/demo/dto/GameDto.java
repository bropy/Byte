package com.steam.demo.dto;

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
    private String title;
    private String description;
    private String avatar;
    private String source;
    private String genre;
    private LocalDate releaseDate;
    private Double price;
    private boolean approved;
    private Long developer;
    private List<AchievementDto> achievements;

    // New fields
    private String type; // Type of the game (e.g., Action, RPG, etc.)
    private String players; // Number of players (e.g., Single-player, Multiplayer)
    private String deviceSupport; // Supported devices (e.g., PC, Console, Mobile)
    private String status; // Current status of the game (e.g., Released, In Development)
}
