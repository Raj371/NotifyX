package com.example.NotifyX.preference.controller;

import com.example.NotifyX.preference.dto.PreferenceResponse;
import com.example.NotifyX.preference.dto.UpdatePreferenceRequest;
import com.example.NotifyX.preference.entity.NotificationChannel;
import com.example.NotifyX.preference.entity.UserChannelPreference;
import com.example.NotifyX.preference.service.UserPreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(
            UserPreferenceService userPreferenceService
    ) {
        this.userPreferenceService = userPreferenceService;
    }

    @GetMapping
    public ResponseEntity<List<PreferenceResponse>> getPreferences(
            @PathVariable UUID userId
    ) {
        List<PreferenceResponse> response =
                userPreferenceService.getPreferences(userId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{channel}")
    public ResponseEntity<PreferenceResponse> updatePreference(
            @PathVariable UUID userId,
            @PathVariable NotificationChannel channel,
            @Valid @RequestBody UpdatePreferenceRequest request
    ) {
        UserChannelPreference updatedPreference =
                userPreferenceService.updatePreference(
                        userId,
                        channel,
                        request.enabled()
                );

        return ResponseEntity.ok(toResponse(updatedPreference));
    }

    private PreferenceResponse toResponse(
            UserChannelPreference preference
    ) {
        return new PreferenceResponse(
                preference.getChannel(),
                preference.isEnabled(),
                preference.getUpdatedAt()
        );
    }
}