package org.example.scheduleevent.public_interface.event;

import org.example.scheduleevent.public_interface.common.PaginationDto;

import java.time.LocalDateTime;
import java.util.Optional;

public record GetEventsDto(
        Optional<Long> organizationId,
        Optional<LocalDateTime> startDateTime,
        Optional<LocalDateTime> endDateTime,
        Optional<String> location,
        Optional<PaginationDto> pagination
) {
}
