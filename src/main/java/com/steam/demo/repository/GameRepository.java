package com.steam.demo.repository;

import com.steam.demo.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}