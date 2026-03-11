package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.Member;
import java.util.List;

@Dao
public interface MemberDao {
    @Query("SELECT * FROM members ORDER BY name ASC")
    LiveData<List<Member>> getAllMembers();

    @Query("SELECT * FROM members WHERE isActive = 1 ORDER BY name ASC")
    LiveData<List<Member>> getActiveMembers();

    @Query("SELECT * FROM members WHERE id = :id")
    LiveData<Member> getMemberById(int id);

    @Query("SELECT * FROM members WHERE name LIKE :name OR phone LIKE :phone")
    LiveData<List<Member>> searchMembers(String name, String phone);

    @Insert
    void insertMember(Member member);

    @Update
    void updateMember(Member member);

    @Delete
    void deleteMember(Member member);

    @Query("SELECT COUNT(*) FROM members WHERE isActive = 1")
    LiveData<Integer> getActiveMemberCount();

    @Query("SELECT * FROM members WHERE membershipExpiry < :currentDate AND isActive = 1")
    LiveData<List<Member>> getExpiredMembers(long currentDate);
}