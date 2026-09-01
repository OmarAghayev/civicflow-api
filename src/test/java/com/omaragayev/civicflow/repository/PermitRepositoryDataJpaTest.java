package com.omaragayev.civicflow.repository;

import com.omaragayev.civicflow.entity.CitizenEntity;
import com.omaragayev.civicflow.entity.PermitApplicationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class PermitRepositoryDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PermitApplicationRepository permitRepository;

    @Test
    @DisplayName("Should persist and find permit application by tracking number with eager citizen fetch")
    void shouldFindPermitByTrackingNumberWithCitizen() {
        // Given
        CitizenEntity citizen = new CitizenEntity("PIN778899", "Alice Smith", "alice@civicflow.local", "+1987654321");
        entityManager.persist(citizen);

        PermitApplicationEntity permit = new PermitApplicationEntity("CV-TEST1234", "COMMERCIAL_SIGNAGE", "SUBMITTED");
        citizen.addPermitApplication(permit);
        entityManager.persist(permit);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<PermitApplicationEntity> found = permitRepository.findByTrackingNumberWithCitizen("CV-TEST1234");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTrackingNumber()).isEqualTo("CV-TEST1234");
        assertThat(found.get().getCitizen()).isNotNull();
        assertThat(found.get().getCitizen().getCitizenPin()).isEqualTo("PIN778899");
    }
}