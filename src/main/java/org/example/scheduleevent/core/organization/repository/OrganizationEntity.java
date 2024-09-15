package org.example.scheduleevent.core.organization.repository;

public record OrganizationEntity(
        Long id,
        String name,
        String googleCalendarId
) {
}
