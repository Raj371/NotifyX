package com.example.NotifyX.preference.entity;

import com.example.NotifyX.User.entity.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_channel_preferences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_channel_preferences_user_channel",
                        columnNames = {"user_id", "channel"}
                )
        }
)
public class UserChannelPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserChannelPreference() {
        // Required by JPA
    }

    public UserChannelPreference(
            User user,
            NotificationChannel channel,
            boolean enabled
    ) {
        this.user = user;
        this.channel = channel;
        this.enabled = enabled;
        this.updatedAt = Instant.now();
    }

    @PrePersist
    @PreUpdate
    protected void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

