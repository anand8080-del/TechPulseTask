package com.techpulsetask.RBAC.controller;

import com.techpulsetask.RBAC.entity.Permissions;
import com.techpulsetask.RBAC.entity.Role;
import com.techpulsetask.RBAC.entity.Rolepermisssion;
import com.techpulsetask.RBAC.repository.PermissionRepository;
import com.techpulsetask.RBAC.repository.RolePermissionRepository;
import com.techpulsetask.RBAC.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * POST /roles/{roleId}/permissions/{permissionId} - grants a permission
 * to a role. THIS endpoint is what makes the whole system dynamic: the
 * moment this succeeds, RbacPermissionEvaluator's DB query starts
 * returning true for that role/permission pair on every subsequent
 * request - with zero code changes.
 */
@RestController
public class RoleControllerPermission {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleControllerPermission(RoleRepository roleRepository,
                                     PermissionRepository permissionRepository,
                                     RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasPermission(null, 'ROLE_PERMISSION_ASSIGN')")
    public ResponseEntity<Rolepermisssion> assignPermissionToRole(@PathVariable Long roleId,
                                                                    @PathVariable Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleId));

        Permissions permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found: " + permissionId));

        if (rolePermissionRepository.existsByRoleAndPermission(role, permission)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already has this permission");
        }

        Rolepermisssion rolePermission = new Rolepermisssion();
        rolePermission.setRole(role);
        rolePermission.setPermissions(permission);
        Rolepermisssion saved = rolePermissionRepository.save(rolePermission);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}