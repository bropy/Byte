package com.steam.demo.repository;

import com.steam.demo.entity.Game;
import com.steam.demo.entity.Guide;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    long countByProfile(Profile profile);
    long countByGame(Game game);
}
