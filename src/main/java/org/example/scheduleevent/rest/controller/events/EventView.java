package org.example.scheduleevent.rest.controller.events;

import java.time.LocalDateTime;
import java.util.Optional;

public record EventView(
        Long id,
        Long organizationId,
        String creatorId,
        String name,
        Optional<String> description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime deadline,
        String locationName
) {
}
