package com.example.NotifyX.preference.exception;

import com.example.NotifyX.preference.entity.NotificationChannel;

import java.util.UUID;

public class PreferenceNotFoundException extends RuntimeException {

    public PreferenceNotFoundException(
            UUID userId,
            NotificationChannel channel
    ) {
        super(
                "Preference not found for user: "
                        + userId
                        + ", channel: "
                        + channel
        );
    }
}

