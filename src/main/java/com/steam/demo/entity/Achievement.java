package com.steam.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement { //новий клас для ачівок у іграх
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "instruction", nullable = false)
    private String instruction;
    @Column(name = "image", nullable = false)
    private String image;
}
