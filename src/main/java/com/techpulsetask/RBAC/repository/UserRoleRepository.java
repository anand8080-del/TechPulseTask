package com.techpulsetask.RBAC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techpulsetask.RBAC.entity.Role;
import com.techpulsetask.RBAC.entity.User;
import com.techpulsetask.RBAC.entity.Userrole;

@Repository
public interface UserRoleRepository extends JpaRepository<Userrole, Long>{

	List<Userrole> findByUser(User user);
	 
    List<Userrole> findByUser_Username(String username);
 
    boolean existsByUserAndRole(User user, Role role);
}
