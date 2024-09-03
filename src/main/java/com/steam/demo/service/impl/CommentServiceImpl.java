package com.steam.demo.service.impl;

import com.steam.demo.entity.Comment;
import com.steam.demo.entity.Profile;
import com.steam.demo.repository.CommentRepository;
import com.steam.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByProfileReceiver(Optional<Profile> profile) {

        return commentRepository.findByProfileReceiver(profile);
    }

}
