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
    public List<Comment> getCommentsByProfileReceiver(Profile profile) {
        return commentRepository.findByProfileReceiver(Optional.ofNullable(profile));
    }

    @Override
    public List<Comment> getCommentsByProfileReceiver(Optional<Profile> profileId) {
        return null;
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}