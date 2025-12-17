package com.startup.doormanagement.service;

import com.startup.doormanagement.dto.DoorRequest;
import com.startup.doormanagement.dto.DoorResponse;
import com.startup.doormanagement.entity.Door;
import com.startup.doormanagement.exception.ResourceNotFoundException;
import com.startup.doormanagement.repository.DoorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoorService {

    private final DoorRepository doorRepository;

    @Transactional(readOnly = true)
    public List<DoorResponse> getAllDoors() {
        return doorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoorResponse getDoorById(Long id) {
        Door door = doorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Door not found with id: " + id));
        return toResponse(door);
    }

    @Transactional
    public DoorResponse createDoor(DoorRequest request) {
        Door door = new Door();
        door.setName(request.getName());
        door.setLocation(request.getLocation());

        Door savedDoor = doorRepository.save(door);
        return toResponse(savedDoor);
    }

    private DoorResponse toResponse(Door door) {
        return new DoorResponse(
                door.getId(),
                door.getName(),
                door.getLocation(),
                door.getCreatedAt()
        );
    }
}


