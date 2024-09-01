package com.steam.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore
    private Profile profileReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_from_id", nullable = false)
    @JsonIgnore
    private Profile profileSender;

    @Column(nullable = false)
    private Timestamp date;
}
