package org.example.scheduleevent.public_interface.event;

import java.time.LocalDateTime;
import java.util.Optional;

public record CreateEventDto(
        String creatorId,
        String name,
        Optional<String> description,
        String location,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime deadlineDateTime,
        Optional<Long> organizationId
) {
}
