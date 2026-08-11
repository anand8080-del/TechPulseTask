package com.techpulsetask.RBAC.security;

import com.techpulsetask.RBAC.repository.RolePermissionRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * The heart of the "no hardcoded role checks" requirement.
 *
 * Instead of @PreAuthorize("hasRole('ADMIN')") - a string baked into the
 * source code - every secured method uses:
 *
 *      @PreAuthorize("hasPermission(null, 'SOME_PERMISSION_NAME')")
 *
 * Spring Security's SpEL evaluator calls THIS class's hasPermission(...)
 * method for that expression. We never branch on a role name in code;
 * we simply ask RolePermissionRepository "does this logged-in user's
 * current set of roles include this permission, right now, in the DB?"
 * Change the DB rows (via the /roles/{id}/permissions/{id} endpoint) and
 * the answer changes on the very next request - no redeploy, no code edit.
 */
@Component
public class RbacPermissionEvaluator implements PermissionEvaluator {

    private final RolePermissionRepository rolePermissionRepository;

    public RbacPermissionEvaluator(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    /**
     * Used for expressions like hasPermission(null, 'SECURE_DATA_READ').
     * targetDomainObject is unused here since our checks are permission-name
     * based rather than tied to a specific loaded entity instance.
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated() || permission == null) {
            return false;
        }

        String username = authentication.getName();
        String permissionName = permission.toString();

        return rolePermissionRepository.userHasPermission(username, permissionName);
    }

    /**
     * Used for expressions like hasPermission(#id, 'Role', 'DELETE') where
     * you're checking permission against a specific entity id/type. Not
     * required by this assignment's endpoints, but implemented to fully
     * satisfy the PermissionEvaluator contract - it just delegates to the
     * same permission-name check above.
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}