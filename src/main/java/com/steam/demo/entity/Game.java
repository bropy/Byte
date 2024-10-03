package com.steam.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Game {
    @jakarta.persistence.Id
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

    @Column(nullable = false)
    private String genre;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    private User developer; // Handles both developer and publisher roles

    // One-to-many relationship with achievements
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Achievement> achievements; // List of achievements associated with the game

    // New fields
    @Column(name = "type", nullable = false)
    private String type; // e.g., Action, Adventure, etc.

    @Column(name = "players", nullable = false)
    private String players; // e.g., Single Player, Multiplayer, etc.

    @Column(name = "device_support", nullable = false)
    private String deviceSupport; // e.g., PC, Console, Mobile, etc.

    @Column(name = "status", nullable = false)
    private String status; // e.g., Released, Upcoming, etc.

}
