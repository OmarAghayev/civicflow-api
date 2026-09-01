package com.omaragayev.civicflow.service;

import com.omaragayev.civicflow.dto.request.PermitApplicationRequest;
import com.omaragayev.civicflow.dto.request.PermitDecisionRequest;
import com.omaragayev.civicflow.dto.response.PermitApplicationResponse;
import com.omaragayev.civicflow.dto.response.PermitDecisionResponse;
import com.omaragayev.civicflow.entity.CitizenEntity;
import com.omaragayev.civicflow.entity.PermitApplicationEntity;
import com.omaragayev.civicflow.event.PermitSubmittedEvent;
import com.omaragayev.civicflow.repository.CitizenRepository;
import com.omaragayev.civicflow.repository.PermitApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PermitService {

    private static final Logger log = LoggerFactory.getLogger(PermitService.class);

    private final CitizenRepository citizenRepository;
    private final PermitApplicationRepository permitRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PermitService(CitizenRepository citizenRepository,
                         PermitApplicationRepository permitRepository,
                         ApplicationEventPublisher eventPublisher) {
        this.citizenRepository = citizenRepository;
        this.permitRepository = permitRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class
    )
    public PermitApplicationResponse processPermitApplication(PermitApplicationRequest request) {
        String trackingNumber = "CV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        CitizenEntity citizen = citizenRepository.findByCitizenPin(request.citizenPin())
                .orElseGet(() -> {
                    log.info("[DB-PERSIST] Registering new citizen entity for PIN: {}", request.citizenPin());
                    String name = request.applicantName() != null ? request.applicantName() : "Citizen-" + request.citizenPin();
                    String mail = request.email() != null ? request.email() : request.citizenPin() + "@civicflow.local";
                    CitizenEntity newCitizen = new CitizenEntity(
                            request.citizenPin(),
                            name,
                            mail,
                            request.phoneNumber()
                    );
                    return citizenRepository.save(newCitizen);
                });

        PermitApplicationEntity application = new PermitApplicationEntity(
                trackingNumber,
                request.permitType(),
                "SUBMITTED"
        );
        citizen.addPermitApplication(application);

        PermitApplicationEntity savedApp = permitRepository.save(application);
        log.info("[DB-PERSIST] Persisted permit application [{}] for citizen [{}]", trackingNumber, citizen.getCitizenPin());

        // Publish Domain Event (Decoupled & Transaction-Aware)
        eventPublisher.publishEvent(new PermitSubmittedEvent(
                savedApp.getTrackingNumber(),
                citizen.getCitizenPin(),
                citizen.getEmail(),
                savedApp.getPermitType(),
                LocalDateTime.now()
        ));

        return new PermitApplicationResponse(
                savedApp.getTrackingNumber(),
                savedApp.getPermitType(),
                savedApp.getStatus(),
                savedApp.getCreatedAt()
        );
    }

    public PermitApplicationResponse getPermitByTrackingNumber(String trackingNumber) {
        PermitApplicationEntity app = permitRepository.findByTrackingNumberWithCitizen(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Permit application not found with tracking number: " + trackingNumber));

        return new PermitApplicationResponse(
                app.getTrackingNumber(),
                app.getPermitType(),
                app.getStatus(),
                app.getCreatedAt()
        );
    }

    public Page<PermitApplicationResponse> getPermitsByStatus(String status, Pageable pageable) {
        return permitRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(app -> new PermitApplicationResponse(
                        app.getTrackingNumber(),
                        app.getPermitType(),
                        app.getStatus(),
                        app.getCreatedAt()
                ));
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class
    )
    public PermitDecisionResponse evaluatePermitDecision(String trackingNumber, PermitDecisionRequest request, String officerUsername) {
        PermitApplicationEntity application = permitRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Permit application not found with tracking number: " + trackingNumber));

        application.setStatus(request.decision());
        application.setDecisionRemarks(request.officerRemarks());

        PermitApplicationEntity updated = permitRepository.save(application);
        log.info("[DECISION] Permit [{}] updated to [{}] by officer [{}]", trackingNumber, updated.getStatus(), officerUsername);

        return new PermitDecisionResponse(
                updated.getTrackingNumber(),
                updated.getStatus(),
                updated.getDecisionRemarks(),
                officerUsername,
                LocalDateTime.now()
        );
    }
}