package com.steam.demo.service;

import com.steam.demo.entity.Comment;
import com.steam.demo.entity.Profile;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getCommentsByProfileReceiver(Optional<Profile> profileId);
}
