package com.steam.demo.repository;

import com.steam.demo.entity.Game;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Review;
import com.steam.demo.entity.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    long countByProfile(Profile profile);
    long countByGame(Game game);
}
