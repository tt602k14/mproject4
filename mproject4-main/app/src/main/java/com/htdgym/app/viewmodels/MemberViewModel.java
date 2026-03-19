package com.htdgym.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.htdgym.app.models.Member;
import com.htdgym.app.repository.GymRepository;
import java.util.List;

public class MemberViewModel extends AndroidViewModel {
    private GymRepository repository;
    private LiveData<List<Member>> allMembers;
    private LiveData<List<Member>> activeMembers;
    private LiveData<Integer> activeMemberCount;

    public MemberViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        allMembers = repository.getAllMembers();
        activeMembers = repository.getActiveMembers();
        activeMemberCount = repository.getActiveMemberCount();
    }

    public LiveData<List<Member>> getAllMembers() {
        return allMembers;
    }

    public LiveData<List<Member>> getActiveMembers() {
        return activeMembers;
    }

    public LiveData<Member> getMemberById(int id) {
        return repository.getMemberById(id);
    }

    public LiveData<Integer> getActiveMemberCount() {
        return activeMemberCount;
    }

    public void insert(Member member) {
        repository.insertMember(member);
    }

    public void update(Member member) {
        repository.updateMember(member);
    }

    public void delete(Member member) {
        repository.deleteMember(member);
    }
}