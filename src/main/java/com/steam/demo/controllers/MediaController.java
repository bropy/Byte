package com.steam.demo.controllers;

import com.steam.demo.dto.ScreenshotDto;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ScreenshotDto>> getRecentScreenshots(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "20") int limit
    ) {
        System.out.println("Start: " + start);
        System.out.println("Limit: " + limit);

        int pageNumber = start;
        System.out.println("Page Number: " + pageNumber);

        PageRequest pageRequest = PageRequest.of(pageNumber, limit);
        System.out.println("Page Request: " + pageRequest);

        Page<Screenshot> recentScreenshots = screenshotRepository.findAllByOrderByDateDesc(pageRequest);
        System.out.println("Recent Screenshots: " + recentScreenshots);

        List<ScreenshotDto> screenshotDTOS = recentScreenshots.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(screenshotDTOS);
    }

    @GetMapping("/screenshots/popular")
    public ResponseEntity<List<ScreenshotDto>> getMostPopularScreenshots(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "20") int limit
    ) {
        int pageNumber = start / limit;
        PageRequest pageRequest = PageRequest.of(pageNumber, limit);
        Page<Screenshot> popularScreenshots = screenshotRepository.findAllByOrderByLikesDesc(pageRequest);
        List<ScreenshotDto> screenshotDTOS = popularScreenshots.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(screenshotDTOS);
    }

    private ScreenshotDto convertToDTO(Screenshot screenshot) {
        ScreenshotDto screenshotDTO = new ScreenshotDto();
        screenshotDTO.setId(screenshot.getId());
        screenshotDTO.setName(screenshot.getName());
        screenshotDTO.setLikes(screenshot.getLikes());
        screenshotDTO.setDislikes(screenshot.getDislikes());
        screenshotDTO.setAward(screenshot.getAward());
        screenshotDTO.setDate(screenshot.getDate());
        screenshotDTO.setDescription(screenshot.getDescription());
        screenshotDTO.setSource(screenshot.getSource());
        screenshotDTO.setGameId(screenshot.getGame().getId());
        screenshotDTO.setProfileId(screenshot.getProfile().getId());
        return screenshotDTO;
    }

}
