package org.example.scheduleevent.core.events.repository.model;

import com.example.schedule_event.public_.tables.records.UserEventRecord;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEventEntityMapper implements RecordMapper<UserEventRecord, UserEventEntity> {
    @Override
    public UserEventEntity map(UserEventRecord userEventRecord) {
        return new UserEventEntity(
                userEventRecord.getEventId(),
                userEventRecord.getUserId()
        );
    }
}
