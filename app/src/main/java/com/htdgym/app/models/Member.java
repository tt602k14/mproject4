package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "members")
public class Member {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Date joinDate;
    private Date membershipExpiry;
    private String membershipType;
    private double balance;
    private String profileImage;
    private boolean isActive;

    // Constructors
    public Member() {}

    @Ignore
    public Member(String name, String phone, String email, String address, 
                 Date joinDate, Date membershipExpiry, String membershipType) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.joinDate = joinDate;
        this.membershipExpiry = membershipExpiry;
        this.membershipType = membershipType;
        this.balance = 0.0;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }

    public Date getMembershipExpiry() { return membershipExpiry; }
    public void setMembershipExpiry(Date membershipExpiry) { this.membershipExpiry = membershipExpiry; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}