package com.startup.doormanagement.service;

import com.startup.doormanagement.dto.DoorRequest;
import com.startup.doormanagement.dto.DoorResponse;
import com.startup.doormanagement.entity.Door;
import com.startup.doormanagement.exception.ResourceNotFoundException;
import com.startup.doormanagement.repository.DoorRepository;
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
class DoorServiceTest {

    @Mock
    private DoorRepository doorRepository;

    @InjectMocks
    private DoorService doorService;

    private Door testDoor;

    @BeforeEach
    void setUp() {
        testDoor = new Door();
        testDoor.setId(1L);
        testDoor.setName("Main Entrance");
        testDoor.setLocation("Building A");
    }

    @Test
    void getAllDoors_ShouldReturnListOfDoors() {
        // Given
        Door door2 = new Door();
        door2.setId(2L);
        door2.setName("Back Door");
        door2.setLocation("Building B");

        when(doorRepository.findAll()).thenReturn(Arrays.asList(testDoor, door2));

        // When
        List<DoorResponse> result = doorService.getAllDoors();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Main Entrance", result.get(0).getName());
        assertEquals("Back Door", result.get(1).getName());
    }

    @Test
    void getDoorById_WhenDoorExists_ShouldReturnDoor() {
        // Given
        when(doorRepository.findById(1L)).thenReturn(Optional.of(testDoor));

        // When
        DoorResponse result = doorService.getDoorById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Main Entrance", result.getName());
        assertEquals("Building A", result.getLocation());
    }

    @Test
    void getDoorById_WhenDoorNotExists_ShouldThrowException() {
        // Given
        when(doorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> doorService.getDoorById(999L));
    }

    @Test
    void createDoor_ShouldCreateDoor() {
        // Given
        DoorRequest request = new DoorRequest("New Door", "Building C");
        when(doorRepository.save(any(Door.class))).thenReturn(testDoor);

        // When
        DoorResponse result = doorService.createDoor(request);

        // Then
        assertNotNull(result);
        verify(doorRepository, times(1)).save(any(Door.class));
    }
}
