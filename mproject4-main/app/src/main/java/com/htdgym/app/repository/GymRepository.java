package com.htdgym.app.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.htdgym.app.database.*;
import com.htdgym.app.models.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GymRepository {
    private MemberDao memberDao;
    private WorkoutDao workoutDao;
    private PaymentDao paymentDao;
    private EquipmentDao equipmentDao;
    private TrainerDao trainerDao;
    private VideoDao videoDao;
    private UserStatsDao userStatsDao;
    private ExecutorService executor;

    public GymRepository(Application application) {
        GymDatabase database = GymDatabase.getInstance(application);
        memberDao = database.memberDao();
        workoutDao = database.workoutDao();
        paymentDao = database.paymentDao();
        equipmentDao = database.equipmentDao();
        trainerDao = database.trainerDao();
        videoDao = database.videoDao();
        userStatsDao = database.userStatsDao();
        executor = Executors.newFixedThreadPool(4);
    }

    // Member operations
    public LiveData<List<Member>> getAllMembers() {
        return memberDao.getAllMembers();
    }

    public LiveData<List<Member>> getActiveMembers() {
        return memberDao.getActiveMembers();
    }

    public LiveData<Member> getMemberById(int id) {
        return memberDao.getMemberById(id);
    }

    public void insertMember(Member member) {
        executor.execute(() -> memberDao.insertMember(member));
    }

    public void updateMember(Member member) {
        executor.execute(() -> memberDao.updateMember(member));
    }

    public void deleteMember(Member member) {
        executor.execute(() -> memberDao.deleteMember(member));
    }

    public LiveData<Integer> getActiveMemberCount() {
        return memberDao.getActiveMemberCount();
    }

    // Workout operations
    public LiveData<List<Workout>> getAllWorkouts() {
        return workoutDao.getAllWorkouts();
    }

    public LiveData<List<Workout>> searchWorkouts(String searchTerm) {
        return workoutDao.searchWorkouts(searchTerm);
    }

    public void insertWorkout(Workout workout) {
        executor.execute(() -> workoutDao.insertWorkout(workout));
    }

    public void updateWorkout(Workout workout) {
        executor.execute(() -> workoutDao.updateWorkout(workout));
    }

    public void deleteWorkout(Workout workout) {
        executor.execute(() -> workoutDao.deleteWorkout(workout));
    }

    // Payment operations
    public LiveData<List<Payment>> getAllPayments() {
        return paymentDao.getAllPayments();
    }

    public LiveData<List<Payment>> getPaymentsByMember(int memberId) {
        return paymentDao.getPaymentsByMember(memberId);
    }

    public LiveData<Double> getTotalRevenueByDateRange(long startDate, long endDate) {
        return paymentDao.getTotalRevenueByDateRange(startDate, endDate);
    }

    public void insertPayment(Payment payment) {
        executor.execute(() -> paymentDao.insertPayment(payment));
    }

    public void updatePayment(Payment payment) {
        executor.execute(() -> paymentDao.updatePayment(payment));
    }

    public void deletePayment(Payment payment) {
        executor.execute(() -> paymentDao.deletePayment(payment));
    }

    // Equipment operations
    public LiveData<List<Equipment>> getAllEquipment() {
        return equipmentDao.getAllEquipment();
    }

    public LiveData<List<Equipment>> getAvailableEquipment() {
        return equipmentDao.getAvailableEquipment();
    }

    public void insertEquipment(Equipment equipment) {
        executor.execute(() -> equipmentDao.insertEquipment(equipment));
    }

    public void updateEquipment(Equipment equipment) {
        executor.execute(() -> equipmentDao.updateEquipment(equipment));
    }

    public void deleteEquipment(Equipment equipment) {
        executor.execute(() -> equipmentDao.deleteEquipment(equipment));
    }

    // Trainer operations
    public LiveData<List<Trainer>> getAllTrainers() {
        return trainerDao.getAllTrainers();
    }

    public LiveData<List<Trainer>> getAvailableTrainers() {
        return trainerDao.getAvailableTrainers();
    }

    public void insertTrainer(Trainer trainer) {
        executor.execute(() -> trainerDao.insertTrainer(trainer));
    }

    public void updateTrainer(Trainer trainer) {
        executor.execute(() -> trainerDao.updateTrainer(trainer));
    }

    public void deleteTrainer(Trainer trainer) {
        executor.execute(() -> trainerDao.deleteTrainer(trainer));
    }

    // Video operations
    public LiveData<List<Video>> getAllVideos() {
        return videoDao.getAllVideos();
    }

    public LiveData<List<Video>> getVideosByCategory(String category) {
        return videoDao.getVideosByCategory(category);
    }

    public LiveData<List<Video>> getSavedVideos() {
        return videoDao.getSavedVideos();
    }

    public void insert(Video video) {
        executor.execute(() -> videoDao.insertVideo(video));
    }

    public void update(Video video) {
        executor.execute(() -> videoDao.updateVideo(video));
    }

    // UserStats operations
    public LiveData<UserStats> getUserStats(int userId) {
        return userStatsDao.getUserStats(userId);
    }

    public void insertUserStats(UserStats stats) {
        executor.execute(() -> userStatsDao.insertUserStats(stats));
    }

    public void updateUserStats(UserStats stats) {
        executor.execute(() -> userStatsDao.updateUserStats(stats));
    }
}