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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessRepository accessRepository;
    private final UserRepository userRepository;
    private final DoorRepository doorRepository;

    @Transactional(readOnly = true)
    public List<AccessResponse> getUsersByDoorId(Long doorId) {
        List<Access> accesses = accessRepository.findByDoorId(doorId);
        return accesses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccessResponse grantAccess(AccessRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Door door = doorRepository.findById(request.getDoorId())
                .orElseThrow(() -> new ResourceNotFoundException("Door not found with id: " + request.getDoorId()));

        if (accessRepository.existsByUserAndDoor(user, door)) {
            throw new DuplicateResourceException("Access already granted for user " + user.getEmail() + " to door " + door.getName());
        }

        Access access = new Access();
        access.setUser(user);
        access.setDoor(door);

        Access savedAccess = accessRepository.save(access);
        return toResponse(savedAccess);
    }

    @Transactional
    public void revokeAccess(AccessRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Door door = doorRepository.findById(request.getDoorId())
                .orElseThrow(() -> new ResourceNotFoundException("Door not found with id: " + request.getDoorId()));

        if (!accessRepository.existsByUserAndDoor(user, door)) {
            throw new ResourceNotFoundException("Access not found for user " + user.getEmail() + " to door " + door.getName());
        }

        accessRepository.deleteByUserAndDoor(user, door);
    }

    private AccessResponse toResponse(Access access) {
        return new AccessResponse(
                access.getId(),
                access.getUser().getId(),
                access.getUser().getName(),
                access.getUser().getEmail(),
                access.getDoor().getId(),
                access.getDoor().getName(),
                access.getDoor().getLocation(),
                access.getGrantedAt()
        );
    }
}


