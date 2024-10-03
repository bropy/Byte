package com.steam.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserGameDto {
    private Long gameId;
    private Long userId;
    private String title;
    private String description;
    private float playtimeHours;
    private LocalDateTime lastPlayed;
    private String status;
    public String avatar;
    public int total;
    private Set<AchievementDto> achievements;
}
