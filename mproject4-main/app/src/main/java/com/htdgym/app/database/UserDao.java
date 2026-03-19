package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.User;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);
    
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    User getUserById(String userId);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    LiveData<User> getUserByEmailLive(String email);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM users WHERE firebaseUid = :uid LIMIT 1")
    LiveData<User> getUserByFirebaseUid(String uid);
    
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    List<User> getAllUsers();
    
    @Query("SELECT COUNT(*) FROM users")
    int getTotalUserCount();
    
    @Query("SELECT COUNT(*) FROM users WHERE isActive = 1")
    int getActiveUserCount();
    
    @Query("SELECT COUNT(*) FROM users WHERE DATE(createdAt/1000, 'unixepoch') = DATE('now')")
    int getNewUsersToday();
    
    @Query("UPDATE users SET isActive = :isActive WHERE userId = :userId")
    void updateUserStatus(String userId, boolean isActive);

    @Insert
    long insertUser(User user);

    @Update
    void update(User user);
    
    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
