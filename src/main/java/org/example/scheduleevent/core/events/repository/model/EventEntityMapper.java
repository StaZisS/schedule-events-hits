package org.example.scheduleevent.core.events.repository.model;

import com.example.schedule_event.public_.tables.records.EventRecord;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventEntityMapper implements RecordMapper<EventRecord, EventEntity> {
    @Override
    public EventEntity map(EventRecord record) {
        return new EventEntity(
                record.getEventId(),
                record.getOrganizationId(),
                record.getCreatorId(),
                record.getName(),
                Optional.ofNullable(record.getDescription()),
                record.getStartDate(),
                record.getEndDate(),
                record.getDeadline(),
                record.getLocationName(),
                record.getGoogleEventId()
        );
    }
}
