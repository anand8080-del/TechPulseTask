package com.techpulsetask.RBAC.controller;

import com.techpulsetask.RBAC.dto.RoleRequest;
import com.techpulsetask.RBAC.entity.Role;
import com.techpulsetask.RBAC.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * POST /roles - creates a new role.
 *
 * Note the permission check: hasPermission(null, 'ROLE_CREATE') - not
 * hasRole('ADMIN'). 'ROLE_CREATE' is just a string that must exist as a
 * row in the permissions table and be linked (via role_permissions) to
 * whatever role the calling user holds. Nothing here hardcodes ADMIN.
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'ROLE_CREATE')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists: " + request.getName());
        }

        Role role = new Role();
        role.setName(request.getName());
        Role saved = roleRepository.save(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}