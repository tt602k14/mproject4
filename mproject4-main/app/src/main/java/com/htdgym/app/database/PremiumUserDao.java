package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.PremiumUser;

import java.util.List;

@Dao
public interface PremiumUserDao {
    
    @Insert
    void insertPremiumUser(PremiumUser premiumUser);
    
    @Update
    void updatePremiumUser(PremiumUser premiumUser);
    
    @Query("SELECT * FROM premium_users WHERE userId = :userId LIMIT 1")
    PremiumUser getPremiumUserByUserId(String userId);
    
    @Query("SELECT * FROM premium_users WHERE isPremium = 1 AND isActive = 1")
    List<PremiumUser> getAllActivePremiumUsers();
    
    @Query("UPDATE premium_users SET isPremium = :isPremium, isActive = :isActive WHERE userId = :userId")
    void updatePremiumStatus(String userId, boolean isPremium, boolean isActive);
    
    @Query("UPDATE premium_users SET isActive = 0 WHERE userId = :userId")
    void deactivatePremium(String userId);
    
    @Query("DELETE FROM premium_users WHERE userId = :userId")
    void deletePremiumUser(String userId);
    
    @Query("SELECT COUNT(*) FROM premium_users WHERE isPremium = 1 AND isActive = 1")
    int getActivePremiumCount();
    
    @Query("SELECT COUNT(*) FROM premium_users WHERE isPremium = 1 AND isActive = 1")
    int getActivePremiumUserCount();
}