package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.PremiumSubscription;

@Dao
public interface PremiumSubscriptionDao {
    @Query("SELECT * FROM premium_subscriptions WHERE userId = :userId AND isActive = 1 LIMIT 1")
    LiveData<PremiumSubscription> getActivePremiumSubscription(int userId);

    @Query("SELECT * FROM premium_subscriptions WHERE userId = :userId AND isActive = 1 LIMIT 1")
    PremiumSubscription getActivePremiumSubscriptionSync(int userId);

    @Insert
    long insertSubscription(PremiumSubscription subscription);

    @Update
    void updateSubscription(PremiumSubscription subscription);

    @Query("UPDATE premium_subscriptions SET isActive = 0 WHERE userId = :userId")
    void deactivateUserSubscriptions(int userId);

    @Query("SELECT COUNT(*) > 0 FROM premium_subscriptions WHERE userId = :userId AND isActive = 1 AND endDate > :currentTime")
    boolean isUserPremium(int userId, long currentTime);
    
    @Query("SELECT * FROM premium_subscriptions WHERE isActive = 1 AND endDate > :currentTime")
    java.util.List<PremiumSubscription> getActivePremiumSubscriptions(long currentTime);
    
    @Query("SELECT COUNT(*) FROM premium_subscriptions WHERE isActive = 1 AND endDate > :currentTime")
    int getActivePremiumCount(long currentTime);
}