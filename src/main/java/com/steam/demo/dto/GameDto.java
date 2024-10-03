package com.steam.demo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private LocalDate releaseDate;
    private Double price;
    private boolean approved;
    private Long developer;
    private List<AchievementDto> achievements;
}