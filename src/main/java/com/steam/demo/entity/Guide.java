package com.steam.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "guide")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private String cover;// обкладинка роботи
    @Column( nullable = false)
    private int likes;
    @Column(nullable = false)
    private int dislikes;
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false)
    private int award; // напевно працює як супер лайк
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
