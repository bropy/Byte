package com.steam.demo.controllers;

import com.steam.demo.entity.CreativeWork;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.Review;
import com.steam.demo.entity.Screenshot;
import com.steam.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ScreenshotRepository screenshotRepository;
    @Autowired
    private ReviewRepository reviewRepositoryRepository;
    @Autowired
    private GuideRepository guideRepository;
    @Autowired
    private CreativeWorkRepository creativeWorkRepository;


    @GetMapping("/media-counter/{profileId}")
    public ResponseEntity<Map<String, Long>> getMediaCountByProfileId(@PathVariable Long profileId) {
        Profile profile = new Profile();
        profile.setId(profileId);
        long videoCount = videoRepository.countByProfile(profile);
        long screenshotCount = screenshotRepository.countByProfile(profile);
        long reviewCount = reviewRepositoryRepository.countByProfile(profile);
        long guideCount = guideRepository.countByProfile(profile);
        long creativeWorkCount = creativeWorkRepository.countByProfile(profile);

        Map<String, Long> mediaCounts = new HashMap<>();
        mediaCounts.put("videoCount", videoCount);
        mediaCounts.put("screenshotCount", screenshotCount);
        mediaCounts.put("reviewCount", reviewCount);
        mediaCounts.put("guideCount", guideCount);
        mediaCounts.put("creativeWorkCount", creativeWorkCount);

        return ResponseEntity.ok(mediaCounts);
    }
    @GetMapping("/screenshots/recent")
    public ResponseEntity<List<Screenshot>> getRecentScreenshots(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "20") int limit
    ) {
        Page<Screenshot> recentScreenshots = screenshotRepository.findTopByOrderByDateDesc(PageRequest.of(start / limit, limit));
        return ResponseEntity.ok(recentScreenshots.getContent());
    }

    @GetMapping("/screenshots/popular")
    public ResponseEntity<List<Screenshot>> getMostPopularScreenshots(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "20") int limit
    ) {
        Page<Screenshot> popularScreenshots = screenshotRepository.findTopByOrderByLikesDesc(PageRequest.of(start / limit, limit));
        return ResponseEntity.ok(popularScreenshots.getContent());
    }

}
