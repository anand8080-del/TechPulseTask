package com.techpulsetask.RBAC.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /secure-data - accessible only if the calling user's roles grant
 * the SECURE_DATA_READ permission (per the assignment's USER role spec:
 * "Accesses secured resources based on assigned permissions").
 *
 * This is the endpoint that best demonstrates dynamic authorization end
 * to end: grant/revoke SECURE_DATA_READ via POST /roles/{id}/permissions/{id}
 * (or remove it) and this endpoint's behavior flips immediately, with no
 * code change or redeploy.
 */
@RestController
public class SecureDataController {

    @GetMapping("/secure-data")
    @PreAuthorize("hasPermission(null, 'SECURE_DATA_READ')")
    public ResponseEntity<String> getSecureData() {
        return ResponseEntity.ok("This is protected data. You were granted access because your role holds the SECURE_DATA_READ permission in the database.");
    }
}