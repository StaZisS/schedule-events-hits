package org.example.scheduleevent.rest.controller.events;

import org.example.scheduleevent.core.events.repository.model.EventEntity;

public class EventMapper {
    public static EventView map(EventEntity entity) {
        return new EventView(
                entity.id(),
                entity.organizationId(),
                entity.creatorId(),
                entity.name(),
                entity.description(),
                entity.startDate(),
                entity.endDate(),
                entity.deadline(),
                entity.locationName()
        );
    }
}
