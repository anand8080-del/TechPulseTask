package com.techpulsetask.RBAC.controller;

import com.techpulsetask.RBAC.entity.Role;
import com.techpulsetask.RBAC.entity.User;
import com.techpulsetask.RBAC.entity.Userrole;
import com.techpulsetask.RBAC.repository.RoleRepository;
import com.techpulsetask.RBAC.repository.UserRepository;
import com.techpulsetask.RBAC.repository.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** POST /users/{userId}/roles/{roleId} - assigns a role to a user. */
@RestController
public class UserRoleController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserRoleController(UserRepository userRepository,
                               RoleRepository roleRepository,
                               UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @PostMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasPermission(null, 'USER_ROLE_ASSIGN')")
    public ResponseEntity<Userrole> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleId));

        if (userRoleRepository.existsByUserAndRole(user, role)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has this role");
        }

        Userrole userRole = new Userrole();
        userRole.setUser(user);
        userRole.setRole(role);
        Userrole saved = userRoleRepository.save(userRole);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}