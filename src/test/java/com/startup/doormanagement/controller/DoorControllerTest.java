package com.startup.doormanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.startup.doormanagement.dto.AccessResponse;
import com.startup.doormanagement.dto.DoorRequest;
import com.startup.doormanagement.dto.DoorResponse;
import com.startup.doormanagement.exception.ResourceNotFoundException;
import com.startup.doormanagement.service.AccessService;
import com.startup.doormanagement.service.DoorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoorController.class)
@Import(com.startup.doormanagement.exception.GlobalExceptionHandler.class)
class DoorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoorService doorService;

    @MockBean
    private AccessService accessService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllDoors_ShouldReturnListOfDoors() throws Exception {
        // Given
        List<DoorResponse> doors = Arrays.asList(
                new DoorResponse(1L, "Main Entrance", "Building A", LocalDateTime.now()),
                new DoorResponse(2L, "Back Door", "Building B", LocalDateTime.now())
        );
        when(doorService.getAllDoors()).thenReturn(doors);

        // When & Then
        mockMvc.perform(get("/api/doors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Main Entrance"))
                .andExpect(jsonPath("$[1].name").value("Back Door"));
    }

    @Test
    void getDoorById_WhenDoorExists_ShouldReturnDoor() throws Exception {
        // Given
        DoorResponse response = new DoorResponse(1L, "Main Entrance", "Building A", LocalDateTime.now());
        when(doorService.getDoorById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/doors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Main Entrance"))
                .andExpect(jsonPath("$.location").value("Building A"));
    }

    @Test
    void getDoorById_WhenDoorNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(doorService.getDoorById(999L)).thenThrow(new ResourceNotFoundException("Door not found with id: 999"));

        // When & Then
        mockMvc.perform(get("/api/doors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Door not found with id: 999"));
    }

    @Test
    void createDoor_WithValidRequest_ShouldReturnCreated() throws Exception {
        // Given
        DoorRequest request = new DoorRequest("New Door", "Building C");
        DoorResponse response = new DoorResponse(1L, "New Door", "Building C", LocalDateTime.now());
        when(doorService.createDoor(any(DoorRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/doors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Door"))
                .andExpect(jsonPath("$.location").value("Building C"));
    }

    @Test
    void getUsersByDoorId_ShouldReturnListOfUsers() throws Exception {
        // Given
        List<AccessResponse> accesses = Arrays.asList(
                new AccessResponse(1L, 1L, "John Doe", "john@example.com", 1L, "Main Entrance", "Building A", LocalDateTime.now())
        );
        when(accessService.getUsersByDoorId(1L)).thenReturn(accesses);

        // When & Then
        mockMvc.perform(get("/api/doors/1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userName").value("John Doe"))
                .andExpect(jsonPath("$[0].doorName").value("Main Entrance"));
    }
}
