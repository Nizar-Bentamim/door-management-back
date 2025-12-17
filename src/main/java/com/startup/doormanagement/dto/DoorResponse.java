package com.startup.doormanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoorResponse {
    private Long id;
    private String name;
    private String location;
    private LocalDateTime createdAt;
}


