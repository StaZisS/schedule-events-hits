package org.example.scheduleevent.rest.controller.events;

import java.time.LocalDateTime;
import java.util.Optional;

public record CreateEventRequest(
        String name,
        Optional<String> description,
        String location,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime deadlineDateTime,
        Optional<Long> organizationId
) {
}
