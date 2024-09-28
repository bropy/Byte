package com.steam.demo.service;

import com.steam.demo.entity.Comment;
import com.steam.demo.entity.Profile;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getCommentsByProfileReceiver(Profile profile);

    List<Comment> getCommentsByProfileReceiver(Optional<Profile> profileId);

    Comment saveComment(Comment comment);

    Optional<Comment> getCommentById(Long id);

    void deleteComment(Long id);

    List<Comment> getAllComments();
}
