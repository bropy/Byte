package com.steam.demo.repository;

import com.steam.demo.entity.CreativeWork;
import com.steam.demo.entity.Game;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreativeWorkRepository extends JpaRepository<CreativeWork, Long> {
    long countByProfile(Profile profile);
    long countByGame(Game game);
}
