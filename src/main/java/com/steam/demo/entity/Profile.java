package com.steam.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String country = "Planet Earth";

    @Column(nullable = true)
    private String status = "Not set";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    // This method ensures that the User ID and Profile ID are equal
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.id = user.getId(); // Ensure the Profile ID matches the User ID
        }
    }
}


