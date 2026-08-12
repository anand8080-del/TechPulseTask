package com.techpulsetask.RBAC.config;

import com.techpulsetask.RBAC.entity.*;
import com.techpulsetask.RBAC.repository.*;

import jakarta.transaction.Transactional;


import jakarta.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the database with an initial ADMIN user so the system is
 * bootstrappable - without this, NO endpoint could ever be called, since
 * every endpoint (including "create a role") itself requires a permission
 * that only an existing ADMIN can grant.
 *
 * This is written in Java (not data.sql) specifically so we can BCrypt-hash
 * the password properly rather than storing/seeding plaintext.
 *
 * Idempotent: checks if ADMIN role already exists before inserting
 * anything, so restarting the app does not create duplicate rows.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                            PermissionRepository permissionRepository,
                            RolePermissionRepository rolePermissionRepository,
                            UserRepository userRepository,
                            UserRoleRepository userRoleRepository,
                            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.existsByName("ADMIN")) {
            return; // already seeded on a previous run - do nothing
        }

        // --- Roles ---
        Role adminRole = roleRepository.save(new Role(null, "ADMIN"));
        Role userRole = roleRepository.save(new Role(null, "USER"));

        // --- Permissions (one per protected action in the API spec) ---
        Permissions roleCreate = permissionRepository.save(new Permissions(0, "ROLE_CREATE"));
        Permissions permissionCreate = permissionRepository.save(new Permissions(0, "PERMISSION_CREATE"));
        Permissions rolePermissionAssign = permissionRepository.save(new Permissions(0, "ROLE_PERMISSION_ASSIGN"));
        Permissions userRoleAssign = permissionRepository.save(new Permissions(0, "USER_ROLE_ASSIGN"));
        Permissions secureDataRead = permissionRepository.save(new Permissions(0, "SECURE_DATA_READ"));

        // --- Grant ADMIN every permission (manages roles/permissions/assignments) ---
        rolePermissionRepository.save(new Rolepermisssion(0, adminRole, roleCreate));
        rolePermissionRepository.save(new Rolepermisssion(0, adminRole, permissionCreate));
        rolePermissionRepository.save(new Rolepermisssion(0, adminRole, rolePermissionAssign));
        rolePermissionRepository.save(new Rolepermisssion(0, adminRole, userRoleAssign));
        rolePermissionRepository.save(new Rolepermisssion(0, adminRole, secureDataRead));

        // --- Grant USER only SECURE_DATA_READ, per the spec: "Accesses
        // secured resources based on assigned permissions" ---
        rolePermissionRepository.save(new Rolepermisssion(0, userRole, secureDataRead));

        // --- Seed one login for each role so you can test both immediately ---
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEnabled(true);
        admin = userRepository.save(admin);
        userRoleRepository.save(new Userrole(0, admin, adminRole));

        User testUser = new User();
        testUser.setUsername("user");
        testUser.setPassword(passwordEncoder.encode("user123"));
        testUser.setEnabled(true);
        testUser = userRepository.save(testUser);
        userRoleRepository.save(new Userrole(0, testUser, userRole));

        System.out.println("=== RBAC seed data created ===");
        System.out.println("ADMIN login -> username: admin / password: admin123");
        System.out.println("USER  login -> username: user  / password: user123");
    }
}
