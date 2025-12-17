package com.startup.doormanagement.controller;

import com.startup.doormanagement.dto.AccessResponse;
import com.startup.doormanagement.dto.DoorRequest;
import com.startup.doormanagement.dto.DoorResponse;
import com.startup.doormanagement.dto.ErrorResponse;
import com.startup.doormanagement.service.AccessService;
import com.startup.doormanagement.service.DoorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Doors", description = "Door management endpoints")
public class DoorController {

    private final DoorService doorService;
    private final AccessService accessService;

    @Operation(summary = "Get all doors", description = "Retrieve a list of all doors in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of doors",
                    content = @Content(schema = @Schema(implementation = DoorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<DoorResponse>> getAllDoors() {
        List<DoorResponse> doors = doorService.getAllDoors();
        return ResponseEntity.ok(doors);
    }

    @Operation(summary = "Get door by ID", description = "Retrieve a specific door by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Door found",
                    content = @Content(schema = @Schema(implementation = DoorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Door not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DoorResponse> getDoorById(@PathVariable Long id) {
        DoorResponse door = doorService.getDoorById(id);
        return ResponseEntity.ok(door);
    }

    @Operation(summary = "Create a new door", description = "Create a new door with name and location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Door created successfully",
                    content = @Content(schema = @Schema(implementation = DoorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<DoorResponse> createDoor(@Valid @RequestBody DoorRequest request) {
        DoorResponse door = doorService.createDoor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(door);
    }

    @Operation(summary = "Get users with access to a door",
               description = "Retrieve all users who have been granted access to a specific door")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users with access",
                    content = @Content(schema = @Schema(implementation = AccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Door not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/users")
    public ResponseEntity<List<AccessResponse>> getUsersByDoorId(@PathVariable Long id) {
        List<AccessResponse> accesses = accessService.getUsersByDoorId(id);
        return ResponseEntity.ok(accesses);
    }
}


