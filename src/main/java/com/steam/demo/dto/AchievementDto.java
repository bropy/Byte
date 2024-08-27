package com.steam.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementDto {
    private Long id;
    private String name;
    private String instruction;
    private String image;
}
