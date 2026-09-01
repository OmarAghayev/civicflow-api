package com.omaragayev.civicflow.service;

import com.omaragayev.civicflow.dto.request.PermitApplicationRequest;
import com.omaragayev.civicflow.dto.response.PermitApplicationResponse;
import com.omaragayev.civicflow.entity.CitizenEntity;
import com.omaragayev.civicflow.entity.PermitApplicationEntity;
import com.omaragayev.civicflow.event.PermitSubmittedEvent;
import com.omaragayev.civicflow.repository.CitizenRepository;
import com.omaragayev.civicflow.repository.PermitApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PermitServiceUnitTest {

    @Mock
    private CitizenRepository citizenRepository;

    @Mock
    private PermitApplicationRepository permitRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PermitService permitService;

    private CitizenEntity mockCitizen;
    private PermitApplicationRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockCitizen = new CitizenEntity("PIN123456", "John Doe", "john.doe@civicflow.local", "+1234567890");
        mockRequest = new PermitApplicationRequest(
                "PIN123456",
                "John Doe",
                "john.doe@civicflow.local",
                "+1234567890",
                "BUILDING_RENOVATION",
                "Residential modification permit"
        );
    }

    @Test
    @DisplayName("Should process permit application, persist entity, and publish domain event")
    void shouldProcessPermitApplicationSuccessfully() {
        // Given
        given(citizenRepository.findByCitizenPin("PIN123456")).willReturn(Optional.of(mockCitizen));
        given(permitRepository.save(any(PermitApplicationEntity.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        PermitApplicationResponse response = permitService.processPermitApplication(mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.permitType()).isEqualTo("BUILDING_RENOVATION");
        assertThat(response.status()).isEqualTo("SUBMITTED");
        assertThat(response.trackingNumber()).startsWith("CV-");

        verify(citizenRepository, times(1)).findByCitizenPin("PIN123456");
        verify(permitRepository, times(1)).save(any(PermitApplicationEntity.class));
        verify(eventPublisher, times(1)).publishEvent(any(PermitSubmittedEvent.class));
    }
}