package com.steam.demo.service;

import com.steam.demo.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByProfileReceiver(Long profileId);
}
