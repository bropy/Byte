package com.steam.demo.service;

import com.steam.demo.entity.Profile;
import com.steam.demo.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public List<Profile> getProfilesByNickname(String nickname) {
        return profileRepository.findByNickname(nickname);
    }

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, Profile profileDetails) {
        Profile profile = profileRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Profile not found")
        );
        profile.setNickname(profileDetails.getNickname());
        profile.setAvatar(profileDetails.getAvatar());
        profile.setDescription(profileDetails.getDescription());
        profile.setCountry(profileDetails.getCountry());
        profile.setStatus(profileDetails.getStatus());

        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}
