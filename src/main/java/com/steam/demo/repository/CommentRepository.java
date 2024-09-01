package com.steam.demo.repository;

import com.steam.demo.entity.Comment;
import com.steam.demo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProfileReceiver(Profile profile);
}
