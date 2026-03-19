package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.util.Date;

@Entity(tableName = "admins")
public class Admin {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "admin_id")
    private String adminId;
    
    @ColumnInfo(name = "username")
    private String username;
    
    @ColumnInfo(name = "email")
    private String email;
    
    @ColumnInfo(name = "password_hash")
    private String passwordHash;
    
    @ColumnInfo(name = "full_name")
    private String fullName;
    
    @ColumnInfo(name = "role")
    private String role; // "super_admin", "admin", "moderator"
    
    @ColumnInfo(name = "permissions")
    private String permissions; // JSON string of permissions
    
    @ColumnInfo(name = "is_active")
    private boolean isActive;
    
    @ColumnInfo(name = "last_login")
    private Date lastLogin;
    
    @ColumnInfo(name = "created_at")
    private Date createdAt;
    
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;
    
    // Constructors
    public Admin() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isActive = true;
    }
    
    @Ignore
    public Admin(String adminId, String username, String email, String passwordHash, 
                 String fullName, String role) {
        this();
        this.adminId = adminId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods
    public boolean isSuperAdmin() {
        return "super_admin".equals(role);
    }
    
    public boolean hasPermission(String permission) {
        if (isSuperAdmin()) return true;
        return permissions != null && permissions.contains(permission);
    }
}