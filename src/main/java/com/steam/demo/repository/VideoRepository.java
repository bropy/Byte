package com.steam.demo.repository;

import com.steam.demo.entity.Video;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    long countByProfile(Profile profile);
    long countByGame(Game game);
}
