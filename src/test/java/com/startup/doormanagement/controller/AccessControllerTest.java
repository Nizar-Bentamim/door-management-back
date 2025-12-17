package com.startup.doormanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.startup.doormanagement.dto.AccessRequest;
import com.startup.doormanagement.dto.AccessResponse;
import com.startup.doormanagement.exception.ResourceNotFoundException;
import com.startup.doormanagement.service.AccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccessController.class)
@Import(com.startup.doormanagement.exception.GlobalExceptionHandler.class)
class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccessService accessService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void grantAccess_WithValidRequest_ShouldReturnCreated() throws Exception {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        AccessResponse response = new AccessResponse(
                1L, 1L, "John Doe", "john@example.com",
                1L, "Main Entrance", "Building A", LocalDateTime.now()
        );
        when(accessService.grantAccess(any(AccessRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.doorName").value("Main Entrance"));
    }

    @Test
    void grantAccess_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        AccessRequest request = new AccessRequest(999L, 1L);
        when(accessService.grantAccess(any(AccessRequest.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 999"));

        // When & Then
        mockMvc.perform(post("/api/access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: 999"));
    }

    @Test
    void revokeAccess_WithValidRequest_ShouldReturnNoContent() throws Exception {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        doNothing().when(accessService).revokeAccess(any(AccessRequest.class));

        // When & Then
        mockMvc.perform(delete("/api/access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void revokeAccess_WhenAccessNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        doThrow(new ResourceNotFoundException("Access not found for user to door"))
                .when(accessService).revokeAccess(any(AccessRequest.class));

        // When & Then
        mockMvc.perform(delete("/api/access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Access not found for user to door"));
    }
}
