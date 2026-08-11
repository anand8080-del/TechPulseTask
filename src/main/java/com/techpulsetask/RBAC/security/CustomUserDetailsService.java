package com.techpulsetask.RBAC.security;

import com.techpulsetask.RBAC.entity.User;
import com.techpulsetask.RBAC.entity.Userrole;
import com.techpulsetask.RBAC.repository.UserRepository;
import com.techpulsetask.RBAC.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bridges our own User/UserRole tables into Spring Security's authentication
 * mechanism. This ONLY handles authentication (who are you, and are your
 * credentials valid) - it is not where authorization decisions are made.
 * Authorization (what are you allowed to do) is entirely delegated to
 * RbacPermissionEvaluator, which is DB-driven and dynamic.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository ;
    private  UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
    	
    	User user = userRepository.findByUsername(username)
    	        .orElseThrow();

        List<Userrole> userRoles = userRoleRepository.findByUser(user);

        List<SimpleGrantedAuthority> authorities = userRoles.stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName()))
                .toList();

        return new UserPrincipal(user, authorities);
    }
}
