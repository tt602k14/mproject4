package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.Admin;

import java.util.List;

@Dao
public interface AdminDao {
    
    @Insert
    long insertAdmin(Admin admin);
    
    @Update
    void updateAdmin(Admin admin);
    
    @Delete
    void deleteAdmin(Admin admin);
    
    @Query("SELECT * FROM admins WHERE email = :email AND password = :password AND isActive = 1 LIMIT 1")
    Admin login(String email, String password);
    
    @Query("SELECT * FROM admins WHERE email = :email LIMIT 1")
    Admin getAdminByEmail(String email);
    
    @Query("SELECT * FROM admins WHERE id = :id LIMIT 1")
    Admin getAdminById(int id);
    
    @Query("SELECT * FROM admins WHERE isActive = 1 ORDER BY createdAt DESC")
    List<Admin> getAllActiveAdmins();
    
    @Query("SELECT * FROM admins ORDER BY createdAt DESC")
    List<Admin> getAllAdmins();
    
    @Query("SELECT COUNT(*) FROM admins WHERE isActive = 1")
    int getActiveAdminCount();
    
    @Query("UPDATE admins SET lastLoginAt = :loginTime WHERE id = :adminId")
    void updateLastLogin(int adminId, long loginTime);
    
    @Query("UPDATE admins SET isActive = :isActive WHERE id = :adminId")
    void updateAdminStatus(int adminId, boolean isActive);
    
    @Query("SELECT EXISTS(SELECT 1 FROM admins WHERE email = :email)")
    boolean isEmailExists(String email);
}