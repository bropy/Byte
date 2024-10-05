package com.steam.demo.controllers;

import com.steam.demo.dto.ScreenshotDto;
import com.steam.demo.entity.*;
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
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ProfileRepository profileRepository;


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

    @PostMapping("/screenshots")
    public ResponseEntity<ScreenshotDto> addScreenshot(@RequestBody ScreenshotDto screenshotDto) {
        try {
            // Convert DTO to entity
            Screenshot screenshot = convertToEntity(screenshotDto);

            // Save screenshot entity
            Screenshot savedScreenshot = screenshotRepository.save(screenshot);

            // Convert saved entity back to DTO
            ScreenshotDto savedScreenshotDto = convertToDTO(savedScreenshot);

            return ResponseEntity.ok(savedScreenshotDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Bad Request in case of an error
        }
    }

    private Screenshot convertToEntity(ScreenshotDto screenshotDto) {
        Screenshot screenshot = new Screenshot();
        screenshot.setName(screenshotDto.getName());
        screenshot.setLikes(screenshotDto.getLikes());
        screenshot.setDislikes(screenshotDto.getDislikes());
        screenshot.setAward(screenshotDto.getAward());
        screenshot.setDate(screenshotDto.getDate());
        screenshot.setDescription(screenshotDto.getDescription());
        screenshot.setSource(screenshotDto.getSource());

        // Fetch related entities
        Profile profile = profileRepository.findById(screenshotDto.getProfileId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID"));
        Game game = gameRepository.findById(screenshotDto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));

        // Set profile and game in the screenshot entity
        screenshot.setProfile(profile);
        screenshot.setGame(game);

        return screenshot;
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
