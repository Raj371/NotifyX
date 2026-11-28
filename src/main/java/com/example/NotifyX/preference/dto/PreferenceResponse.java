package com.example.NotifyX.preference.dto;

import com.example.NotifyX.preference.entity.NotificationChannel;

import java.time.Instant;

public record PreferenceResponse(
        NotificationChannel channel,
        boolean enabled,
        Instant updatedAt
) {}

