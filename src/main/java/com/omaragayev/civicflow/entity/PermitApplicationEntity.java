package com.omaragayev.civicflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "permit_applications", indexes = {
        @Index(name = "idx_tracking_number", columnList = "tracking_number", unique = true),
        @Index(name = "idx_permit_status", columnList = "status")
})
public class PermitApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", nullable = false, unique = true, length = 30)
    private String trackingNumber;

    @Column(name = "permit_type", nullable = false, length = 50)
    private String permitType;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "decision_remarks", length = 500)
    private String decisionRemarks;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "citizen_id", nullable = false)
    private CitizenEntity citizen;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public PermitApplicationEntity() {}

    public PermitApplicationEntity(String trackingNumber, String permitType, String status) {
        this.trackingNumber = trackingNumber;
        this.permitType = permitType;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getPermitType() { return permitType; }
    public void setPermitType(String permitType) { this.permitType = permitType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDecisionRemarks() { return decisionRemarks; }
    public void setDecisionRemarks(String decisionRemarks) { this.decisionRemarks = decisionRemarks; }
    public CitizenEntity getCitizen() { return citizen; }
    public void setCitizen(CitizenEntity citizen) { this.citizen = citizen; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}