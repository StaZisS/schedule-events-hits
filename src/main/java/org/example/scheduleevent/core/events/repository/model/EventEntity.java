package org.example.scheduleevent.core.events.repository.model;

import java.time.LocalDateTime;
import java.util.Optional;

public record EventEntity(
        Long id,
        Long organizationId,
        String creatorId,
        String name,
        Optional<String> description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime deadline,
        String locationName,
        String googleCalendarEventId
) {
}
