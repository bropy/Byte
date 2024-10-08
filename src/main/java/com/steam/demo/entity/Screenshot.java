package com.steam.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "screenshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Screenshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int likes;
    @Column(nullable = false)
    private int dislikes;
    @Column(nullable = false)
    private int award; // напевно працює як супер лайк
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String source; // лінк на фото
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
