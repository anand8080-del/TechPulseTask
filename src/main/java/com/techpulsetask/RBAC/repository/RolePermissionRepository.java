package com.techpulsetask.RBAC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techpulsetask.RBAC.entity.Permissions;
import com.techpulsetask.RBAC.entity.Role;
import com.techpulsetask.RBAC.entity.Rolepermisssion;

@Repository
public interface RolePermissionRepository extends JpaRepository<Rolepermisssion, Long>{

	
	List<Rolepermisssion> findByRole(Role role);
	 
    boolean existsByRoleAndPermission(Role role, Permissions permission);
    
    @Query("""
            SELECT COUNT(rp) > 0
            FROM RolePermisssion rp
            WHERE rp.permission.name = :permissionName
              AND rp.role.id IN (
                  SELECT ur.role.id
                  FROM Userrole ur
                  WHERE ur.user.username = :username
              )
            """)
    boolean userHasPermission(@Param("username") String username,
            @Param("permissionName") String permissionName);
}
