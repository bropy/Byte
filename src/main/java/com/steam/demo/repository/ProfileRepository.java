package com.steam.demo.repository;

import com.steam.demo.entity.Profile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByNickname(String nickname);
}
