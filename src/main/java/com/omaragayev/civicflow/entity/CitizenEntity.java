package com.omaragayev.civicflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citizens", indexes = {
        @Index(name = "idx_citizen_pin", columnList = "citizen_pin", unique = true)
})
public class CitizenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_pin", nullable = false, unique = true, length = 20)
    private String citizenPin;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @OneToMany(mappedBy = "citizen", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PermitApplicationEntity> permitApplications = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public CitizenEntity() {}

    public CitizenEntity(String citizenPin, String fullName, String email, String phoneNumber) {
        this.citizenPin = citizenPin;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addPermitApplication(PermitApplicationEntity application) {
        permitApplications.add(application);
        application.setCitizen(this);
    }

    public Long getId() { return id; }
    public String getCitizenPin() { return citizenPin; }
    public void setCitizenPin(String citizenPin) { this.citizenPin = citizenPin; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public List<PermitApplicationEntity> getPermitApplications() { return permitApplications; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}