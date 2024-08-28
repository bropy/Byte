package com.steam.demo.repository;

import com.steam.demo.entity.Screenshot;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {
    long countByProfile(Profile profile);
    long countByGame(Game game);
}
