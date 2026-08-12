# RBAC - Role Based Access Control

This is a Spring Boot REST API project developed to implement Role Based Access Control (RBAC).

The main purpose of this project is to control what users can access based on their roles and permissions.

For example, different users can have different roles, and each role can have different permissions for accessing different resources.

## Technologies Used

- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- REST API
- Bean Validation
- Spring Actuator
- Git & GitHub

## Project Features

- User management
- Role management
- Permission management
- Assign roles to users
- Assign permissions to roles
- Role based authorization
- Permission based authorization
- Secure REST APIs
- Custom Spring Security configuration
- Password encryption using BCrypt
- DTOs for request data
- Global security configuration
- H2 database for storing application data
- Actuator for application monitoring

## What is RBAC?

RBAC stands for Role Based Access Control.

In this project, access to the application is controlled using users, roles and permissions.

A user can have one or more roles, and roles can have different permissions.

For example:

- Admin can have permissions to create, update and delete data.
- Manager can have permissions to view and update data.
- Normal users can have limited access.

This makes it easier to manage authorization in an application.

## How Security Works

The project uses Spring Security to authenticate users and authorize requests.

The application contains custom security classes such as:

- `CustomUserDetailsService`
- `UserPrincipal`
- `RbacPermissionEvaluator`
- `SecurityConfig`
- `MethodSecurityConfig`
- `PasswordConfig`

These classes are used to configure authentication, authorization and password encoding.

## Main Entities

The project contains the following main entities:

- `User`
- `Role`
- `Permissions`
- `Userrole`
- `Rolepermisssion`

These entities are used to maintain the relationship between users, roles and permissions.

## Controllers

The project contains controllers for managing users, roles and permissions.

Main controllers include:

- `PermissionController`
- `RoleController`
- `RoleControllerPermission`
- `UserRoleController`
- `SecureDataController`

The `SecureDataController` is used to demonstrate protected API access.

## Repository Layer

Spring Data JPA repositories are used to communicate with the database.

The project contains repositories for:

- Users
- Roles
- Permissions
- User roles
- Role permissions

This keeps database operations separate from the controller and security logic.

## DTOs

DTOs are used to receive request data from the client.

The project contains:

- `PermissionRequest`
- `RoleRequest`

Using DTOs helps to keep request data separate from entity classes.

## Password Security

Passwords are not stored directly as plain text.

The project uses BCrypt password encoding to securely store passwords.

The password configuration is handled in:

```text
PasswordConfig.java
