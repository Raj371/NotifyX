
CREATE TABLE user_channel_preferences (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    channel VARCHAR(20) NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_user_channel_preferences_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_user_channel_preferences_user_channel
        UNIQUE (user_id, channel),

    CONSTRAINT chk_user_channel_preferences_channel
        CHECK (channel IN ('EMAIL', 'SMS', 'PUSH'))
);

CREATE INDEX idx_user_channel_preferences_user_id
    ON user_channel_preferences(user_id);
