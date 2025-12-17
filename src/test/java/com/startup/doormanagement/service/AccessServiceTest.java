package com.startup.doormanagement.service;

import com.startup.doormanagement.dto.AccessRequest;
import com.startup.doormanagement.dto.AccessResponse;
import com.startup.doormanagement.entity.Access;
import com.startup.doormanagement.entity.Door;
import com.startup.doormanagement.entity.User;
import com.startup.doormanagement.exception.DuplicateResourceException;
import com.startup.doormanagement.exception.ResourceNotFoundException;
import com.startup.doormanagement.repository.AccessRepository;
import com.startup.doormanagement.repository.DoorRepository;
import com.startup.doormanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessServiceTest {

    @Mock
    private AccessRepository accessRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DoorRepository doorRepository;

    @InjectMocks
    private AccessService accessService;

    private User testUser;
    private Door testDoor;
    private Access testAccess;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        testDoor = new Door();
        testDoor.setId(1L);
        testDoor.setName("Main Entrance");
        testDoor.setLocation("Building A");

        testAccess = new Access();
        testAccess.setId(1L);
        testAccess.setUser(testUser);
        testAccess.setDoor(testDoor);
    }

    @Test
    void getUsersByDoorId_ShouldReturnListOfAccesses() {
        // Given
        when(accessRepository.findByDoorId(1L)).thenReturn(Arrays.asList(testAccess));

        // When
        List<AccessResponse> result = accessService.getUsersByDoorId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getUserName());
        assertEquals("Main Entrance", result.get(0).getDoorName());
    }

    @Test
    void grantAccess_WhenValid_ShouldGrantAccess() {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(doorRepository.findById(1L)).thenReturn(Optional.of(testDoor));
        when(accessRepository.existsByUserAndDoor(testUser, testDoor)).thenReturn(false);
        when(accessRepository.save(any(Access.class))).thenReturn(testAccess);

        // When
        AccessResponse result = accessService.grantAccess(request);

        // Then
        assertNotNull(result);
        verify(accessRepository, times(1)).save(any(Access.class));
    }

    @Test
    void grantAccess_WhenUserNotFound_ShouldThrowException() {
        // Given
        AccessRequest request = new AccessRequest(999L, 1L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> accessService.grantAccess(request));
        verify(accessRepository, never()).save(any(Access.class));
    }

    @Test
    void grantAccess_WhenDoorNotFound_ShouldThrowException() {
        // Given
        AccessRequest request = new AccessRequest(1L, 999L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(doorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> accessService.grantAccess(request));
        verify(accessRepository, never()).save(any(Access.class));
    }

    @Test
    void grantAccess_WhenAccessAlreadyExists_ShouldThrowException() {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(doorRepository.findById(1L)).thenReturn(Optional.of(testDoor));
        when(accessRepository.existsByUserAndDoor(testUser, testDoor)).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> accessService.grantAccess(request));
        verify(accessRepository, never()).save(any(Access.class));
    }

    @Test
    void revokeAccess_WhenValid_ShouldRevokeAccess() {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(doorRepository.findById(1L)).thenReturn(Optional.of(testDoor));
        when(accessRepository.existsByUserAndDoor(testUser, testDoor)).thenReturn(true);

        // When
        accessService.revokeAccess(request);

        // Then
        verify(accessRepository, times(1)).deleteByUserAndDoor(testUser, testDoor);
    }

    @Test
    void revokeAccess_WhenUserNotFound_ShouldThrowException() {
        // Given
        AccessRequest request = new AccessRequest(999L, 1L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> accessService.revokeAccess(request));
        verify(accessRepository, never()).deleteByUserAndDoor(any(), any());
    }

    @Test
    void revokeAccess_WhenDoorNotFound_ShouldThrowException() {
        // Given
        AccessRequest request = new AccessRequest(1L, 999L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(doorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> accessService.revokeAccess(request));
        verify(accessRepository, never()).deleteByUserAndDoor(any(), any());
    }

    @Test
    void revokeAccess_WhenAccessNotFound_ShouldThrowException() {
        // Given
        AccessRequest request = new AccessRequest(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(doorRepository.findById(1L)).thenReturn(Optional.of(testDoor));
        when(accessRepository.existsByUserAndDoor(testUser, testDoor)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> accessService.revokeAccess(request));
        verify(accessRepository, never()).deleteByUserAndDoor(any(), any());
    }
}
