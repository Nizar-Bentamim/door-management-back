package com.startup.doormanagement.controller;

import com.startup.doormanagement.dto.AccessRequest;
import com.startup.doormanagement.dto.AccessResponse;
import com.startup.doormanagement.dto.ErrorResponse;
import com.startup.doormanagement.service.AccessService;
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

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Access Control", description = "Access management endpoints for granting and revoking user access to doors")
public class AccessController {

    private final AccessService accessService;

    @Operation(summary = "Grant access to a user",
               description = "Grant a user access to a specific door. Creates an access relationship between the user and door.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Access granted successfully",
                    content = @Content(schema = @Schema(implementation = AccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Access already exists or invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or door not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AccessResponse> grantAccess(@Valid @RequestBody AccessRequest request) {
        AccessResponse access = accessService.grantAccess(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(access);
    }

    @Operation(summary = "Revoke access from a user",
               description = "Revoke a user's access to a specific door. Removes the access relationship between the user and door.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Access revoked successfully"),
            @ApiResponse(responseCode = "404", description = "User, door, or access relationship not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Void> revokeAccess(@Valid @RequestBody AccessRequest request) {
        accessService.revokeAccess(request);
        return ResponseEntity.noContent().build();
    }
}


