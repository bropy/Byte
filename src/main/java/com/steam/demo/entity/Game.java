package com.steam.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String avatar;
    @Column(nullable = false)
    private String source;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean approved;

    @ManyToMany
    @JoinTable(
            name = "game_developers",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id")
    )
    private Set<User> developers;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;
}
