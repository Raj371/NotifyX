package com.example.NotifyX.preference.service;

import com.example.NotifyX.preference.entity.NotificationChannel;
import com.example.NotifyX.preference.entity.UserChannelPreference;
import com.example.NotifyX.preference.repository.UserChannelPreferenceRepository;
import com.example.NotifyX.User.entity.User;
import com.example.NotifyX.User.exception.UserNotFoundException;
import com.example.NotifyX.User.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserPreferenceService {

    private final UserChannelPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public UserPreferenceService(
            UserChannelPreferenceRepository preferenceRepository,
            UserRepository userRepository
    ) {
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createDefaultPreferences(User user) {
        List<UserChannelPreference> preferences =
                Arrays.stream(NotificationChannel.values())
                        .map(channel ->
                                new UserChannelPreference(user, channel, true))
                        .toList();

        preferenceRepository.saveAll(preferences);
    }

    @Transactional(readOnly = true)
    public List<UserChannelPreference> getPreferences(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Map<NotificationChannel, UserChannelPreference> existingPreferences =
                new EnumMap<>(NotificationChannel.class);

        preferenceRepository.findAllByUserId(userId)
                .forEach(preference ->
                        existingPreferences.put(
                                preference.getChannel(),
                                preference
                        )
                );

        return Arrays.stream(NotificationChannel.values())
                .map(channel -> {
                    UserChannelPreference existing =
                            existingPreferences.get(channel);

                    if (existing != null) {
                        return existing;
                    }

                    User user = userRepository.getReferenceById(userId);

                    return new UserChannelPreference(user, channel, true);
                })
                .toList();
    }

    @Transactional
    public UserChannelPreference updatePreference(
            UUID userId,
            NotificationChannel channel,
            boolean enabled
    ) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        preferenceRepository.upsertPreference(
                userId,
                channel.name(),
                enabled
        );

        return preferenceRepository
                .findByUserIdAndChannel(userId, channel)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Preference missing after upsert: " + channel
                        ));
    }
}