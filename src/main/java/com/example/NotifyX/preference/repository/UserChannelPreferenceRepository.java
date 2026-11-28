package com.example.NotifyX.preference.repository;

import com.example.NotifyX.preference.entity.NotificationChannel;
import com.example.NotifyX.preference.entity.UserChannelPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserChannelPreferenceRepository
        extends JpaRepository<UserChannelPreference, UUID> {

    List<UserChannelPreference> findAllByUserId(UUID userId);

    Optional<UserChannelPreference> findByUserIdAndChannel(
            UUID userId,
            NotificationChannel channel
    );

    boolean existsByUserId(UUID userId);

    @Modifying
    @Query(value = """
        INSERT INTO user_channel_preferences (
            id, user_id, channel, enabled, updated_at
        )
        VALUES (
            gen_random_uuid(),
            :userId,
            :channel,
            :enabled,
            CURRENT_TIMESTAMP
        )
        ON CONFLICT (user_id, channel)
        DO UPDATE SET
            enabled = EXCLUDED.enabled,
            updated_at = CURRENT_TIMESTAMP
        """, nativeQuery = true)
    void upsertPreference(
            @Param("userId") UUID userId,
            @Param("channel") String channel,
            @Param("enabled") boolean enabled
    );
}

