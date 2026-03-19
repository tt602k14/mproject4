package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String profileImage;
    private String loginType; // email, google, facebook, apple
    private String firebaseUid;
    private long createdAt;
    
    // Additional fields for admin management
    private int age;
    private float weight;
    private float height;
    private String fitnessGoal;
    private long lastLoginTime;
    private boolean active = true;

    // Default constructor (required by Room)
    public User() {
        this.loginType = "email";
        this.createdAt = System.currentTimeMillis();
        this.lastLoginTime = System.currentTimeMillis();
        this.active = true;
    }

    // Constructor for easy creation
    @Ignore
    public User(String email, String password, String name) {
        this();
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }

    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    // Convenience method for fullName (uses name field)
    public String getFullName() { return name; }
    public void setFullName(String fullName) { this.name = fullName; }
    
    // Additional getters and setters for admin management
    public String getPhoneNumber() { return phone; }
    public void setPhoneNumber(String phoneNumber) { this.phone = phoneNumber; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
    
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
    
    public String getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }
    
    public long getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(long lastLoginTime) { this.lastLoginTime = lastLoginTime; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
