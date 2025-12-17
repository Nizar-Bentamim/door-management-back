package com.startup.doormanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long doorId;
    private String doorName;
    private String doorLocation;
    private LocalDateTime grantedAt;
}


