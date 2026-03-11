package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);
    
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
    void update(User user);
    
    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
