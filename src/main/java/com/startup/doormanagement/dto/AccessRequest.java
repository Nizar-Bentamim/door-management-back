package com.startup.doormanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Door ID is required")
    private Long doorId;
}


