package com.steam.demo.controllers;

import com.steam.demo.entity.Comment;
import com.steam.demo.entity.Profile;
import com.steam.demo.service.CommentService;
import com.steam.demo.service.ProfileService;
import com.steam.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getProfileComments(@PathVariable Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        if (profile.isPresent()) {
            List<Comment> comments = commentService.getCommentsByProfileReceiver(Optional.of(profile.get()));
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addCommentToProfile(
            @PathVariable Long id,
            @RequestBody Comment comment,
            HttpServletRequest request
    ) {
        Long currentUserId = (Long) request.getSession().getAttribute("userId");
        Optional<Profile> receiverProfile = profileService.getProfileById(id);
        Optional<Profile> senderProfile = profileService.getProfileByUserId(currentUserId);

        if (receiverProfile.isPresent() && senderProfile.isPresent()) {
            comment.setProfileReceiver(receiverProfile.get());
            comment.setProfileSender(senderProfile.get()); // Set the user who is leaving the comment
            comment.setDate(Timestamp.from(Instant.now()));

            Comment savedComment = commentService.saveComment(comment);
            return ResponseEntity.ok(savedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/search/{nickname}")
    public List<Profile> getProfilesByNickname(@PathVariable String nickname) {
        return profileService.getProfilesByNickname(nickname);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.createProfile(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long id,
            @RequestBody Profile profileDetails
    ) {
        Optional<Profile> existingProfile = profileService.getProfileById(id);

        if (existingProfile.isPresent()) {
            Profile updatedProfile = existingProfile.get();

            // Update the profile fields only if they are not empty
            if (profileDetails.getAvatar() != null) {
                updatedProfile.setAvatar(profileDetails.getAvatar());
            }
            if (profileDetails.getDescription() != null) {
                updatedProfile.setDescription(profileDetails.getDescription());
            }
            if (profileDetails.getCountry() != null) {
                updatedProfile.setCountry(profileDetails.getCountry());
            }
            if (profileDetails.getStatus() != null) {
                updatedProfile.setStatus(profileDetails.getStatus());
            }

            updatedProfile = profileService.updateProfile(id, updatedProfile);
            return ResponseEntity.ok(updatedProfile);
        }
        return ResponseEntity.notFound().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}