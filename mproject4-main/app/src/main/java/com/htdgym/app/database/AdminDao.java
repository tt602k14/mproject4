package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.Admin;

import java.util.Date;
import java.util.List;

@Dao
public interface AdminDao {
    
    @Insert
    long insertAdmin(Admin admin);
    
    @Update
    void updateAdmin(Admin admin);
    
    @Delete
    void deleteAdmin(Admin admin);
    
    @Query("SELECT * FROM admins WHERE id = :id")
    Admin getAdminById(int id);
    
    @Query("SELECT * FROM admins WHERE admin_id = :adminId")
    Admin getAdminByAdminId(String adminId);
    
    @Query("SELECT * FROM admins WHERE username = :username")
    Admin getAdminByUsername(String username);
    
    @Query("SELECT * FROM admins WHERE email = :email")
    Admin getAdminByEmail(String email);
    
    @Query("SELECT * FROM admins WHERE username = :username AND password_hash = :passwordHash")
    Admin authenticateAdmin(String username, String passwordHash);
    
    @Query("SELECT * FROM admins WHERE is_active = 1 ORDER BY created_at DESC")
    List<Admin> getAllActiveAdmins();
    
    @Query("SELECT * FROM admins ORDER BY created_at DESC")
    List<Admin> getAllAdmins();
    
    @Query("SELECT * FROM admins WHERE role = :role AND is_active = 1")
    List<Admin> getAdminsByRole(String role);
    
    @Query("UPDATE admins SET last_login = :lastLogin WHERE id = :adminId")
    void updateLastLogin(int adminId, Date lastLogin);
    
    @Query("UPDATE admins SET is_active = :isActive WHERE id = :adminId")
    void updateAdminStatus(int adminId, boolean isActive);
    
    @Query("UPDATE admins SET password_hash = :newPasswordHash WHERE id = :adminId")
    void updateAdminPassword(int adminId, String newPasswordHash);
    
    @Query("SELECT COUNT(*) FROM admins WHERE is_active = 1")
    int getActiveAdminCount();
    
    @Query("SELECT COUNT(*) FROM admins WHERE role = 'super_admin' AND is_active = 1")
    int getSuperAdminCount();
    
    @Query("DELETE FROM admins WHERE is_active = 0 AND created_at < :cutoffDate")
    void deleteInactiveAdmins(Date cutoffDate);
}