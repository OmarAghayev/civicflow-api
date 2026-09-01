package com.omaragayev.civicflow.repository;

import com.omaragayev.civicflow.entity.PermitApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermitApplicationRepository extends JpaRepository<PermitApplicationEntity, Long> {

    Optional<PermitApplicationEntity> findByTrackingNumber(String trackingNumber);

    Page<PermitApplicationEntity> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    List<PermitApplicationEntity> findByStatus(String status);

    @Query("SELECT p FROM PermitApplicationEntity p JOIN FETCH p.citizen WHERE p.trackingNumber = :trackingNumber")
    Optional<PermitApplicationEntity> findByTrackingNumberWithCitizen(@Param("trackingNumber") String trackingNumber);

    // Finds stale submitted applications older than a cutoff threshold for batch cleanup
    @Query("SELECT p FROM PermitApplicationEntity p WHERE p.status = :status AND p.createdAt < :threshold")
    Page<PermitApplicationEntity> findStaleApplications(
            @Param("status") String status,
            @Param("threshold") LocalDateTime threshold,
            Pageable pageable
    );
}