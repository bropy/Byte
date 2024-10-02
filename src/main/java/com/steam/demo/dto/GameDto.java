package com.steam.demo.dto;

import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime releaseDate;
    private double price;
    private boolean approved;
    private Long developer;
}