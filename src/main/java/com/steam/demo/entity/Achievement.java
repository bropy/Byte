package com.steam.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String instruction;

    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game; // The game associated with this achievement
}
