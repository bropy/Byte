package com.steam.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ScreenshotDto {
    private Long id;
    private String name;
    private int likes;
    private int dislikes;
    private int award;
    private Timestamp date;
    private String description;
    private String source;
    private Long gameId;
    private Long profileId;
}
