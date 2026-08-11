package com.techpulsetask.RBAC.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "role_permissions",
uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "permission_id"}))
public class Rolepermisssion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	 @ManyToOne(fetch = FetchType.LAZY, optional = false)
	 @JoinColumn(name = "role_id", nullable = false)
	private Role role;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
	private Permissions permissions;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}
	
	public Rolepermisssion()
	{
		
	}

	public Rolepermisssion(long id, Role role, Permissions permissions) {
		super();
		this.id = id;
		this.role = role;
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Rolepermisssion [id=" + id + ", role=" + role + ", permissions=" + permissions + "]";
	}
	
	

}
