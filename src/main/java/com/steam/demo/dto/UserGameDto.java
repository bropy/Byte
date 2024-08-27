package com.steam.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserGameDto {
    private Long gameId;
    private String title;
    private String description;
    private float playtimeHours;
    private LocalDateTime lastPlayed;
    private String status;
    private Set<AchievementDto> achievements;
}
