package com.techpulsetask.RBAC.controller;

import com.techpulsetask.RBAC.dto.PermissionRequest;
import com.techpulsetask.RBAC.entity.Permissions;
import com.techpulsetask.RBAC.repository.PermissionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** POST /permissions - creates a new permission. */
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionRepository permissionRepository;

    public PermissionController(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'PERMISSION_CREATE')")
    public ResponseEntity<Permissions> createPermission(@Valid @RequestBody PermissionRequest request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists: " + request.getName());
        }

        Permissions permission = new Permissions();
        permission.setName(request.getName());
        Permissions saved = permissionRepository.save(permission);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}