package com.steam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGames { //бібліотека юзера

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "playtime_hours", nullable = false)
    private float playtimeHours;
    //я думаю що поле ігноред не треба, якщо час в грі дорівнює 0, то
    //нащо це тримати в якомусь полі

    @Column(name = "last_played")
    private LocalDateTime lastPlayed;
    @Column(name = "status")//in wish list\bought\gifted\owned
    private String status;
}
