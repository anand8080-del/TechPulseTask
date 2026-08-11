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
@Table(name = "user_roles",
uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class Userrole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	 @ManyToOne(fetch = FetchType.LAZY, optional = false)
	 @JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	 @ManyToOne(fetch = FetchType.LAZY, optional = false)
	 @JoinColumn(name = "role_id", nullable = false)
	private Role role;

	 
	 public long getId() {
		 return id;
	 }

	 public void setId(long id) {
		 this.id = id;
	 }

	 public User getUser() {
		 return user;
	 }

	 public void setUser(User user) {
		 this.user = user;
	 }

	 public Role getRole() {
		 return role;
	 }

	 public void setRole(Role role) {
		 this.role = role;
	 }
	 
	 public Userrole()
	 {
		 
	 }

	 public Userrole(long id, User user, Role role) {
		super();
		this.id = id;
		this.user = user;
		this.role = role;
	 }

	 
	 @Override
	 public String toString() {
		return "Userrole [id=" + id + ", user=" + user + ", role=" + role + "]";
	 }
	 
	 
	 
}
