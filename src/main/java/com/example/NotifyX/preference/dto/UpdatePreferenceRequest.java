package com.example.NotifyX.preference.dto;

import jakarta.validation.constraints.NotNull;

public record UpdatePreferenceRequest(

        @NotNull(message = "Enabled field is required")
        Boolean enabled

) {}

