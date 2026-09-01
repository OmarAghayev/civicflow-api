package com.omaragayev.civicflow.repository;

import com.omaragayev.civicflow.entity.CitizenEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<CitizenEntity, Long> {

    Optional<CitizenEntity> findByCitizenPin(String citizenPin);

    // Uses idx_citizens_email_lower functional index
    @Query("SELECT c FROM CitizenEntity c WHERE LOWER(c.email) = LOWER(:email)")
    Optional<CitizenEntity> findByEmailIgnoreCase(@Param("email") String email);

    // Prevents N+1 query problem via JOIN FETCH
    @Query("SELECT c FROM CitizenEntity c JOIN FETCH c.permitApplications WHERE c.citizenPin = :citizenPin")
    Optional<CitizenEntity> findByCitizenPinWithPermitsFetchJoin(@Param("citizenPin") String citizenPin);

    // Declarative N+1 prevention using Spring Data JPA @EntityGraph
    @EntityGraph(attributePaths = {"permitApplications"})
    Optional<CitizenEntity> findWithPermitsByCitizenPin(String citizenPin);
}