package org.example.scheduleevent.core.manager.repository;

import org.example.scheduleevent.public_interface.manager.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationForMembershipEntity(
        Long applicationId,
        Long organizationId,
        String managerId,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
}
