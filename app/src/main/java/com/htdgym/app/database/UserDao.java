package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    LiveData<User> getUserByEmailLive(String email);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM users WHERE firebaseUid = :uid LIMIT 1")
    LiveData<User> getUserByFirebaseUid(String uid);

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();
    
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    java.util.List<User> getAllUsers();
    
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
    
    // Simplified methods for admin management - just return all users for now
    // In real implementation, you would need to join with premium_subscriptions table
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    java.util.List<User> getPremiumUsers();
    
    @Query("SELECT * FROM users ORDER BY createdAt DESC") 
    java.util.List<User> getFreeUsers();
}
