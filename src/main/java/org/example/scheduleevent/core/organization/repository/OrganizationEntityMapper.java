package org.example.scheduleevent.core.organization.repository;

import com.example.schedule_event.public_.tables.records.OrganizationRecord;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

@Component
public class OrganizationEntityMapper implements RecordMapper<OrganizationRecord, OrganizationEntity> {
    @Override
    public OrganizationEntity map(OrganizationRecord record) {
        return new OrganizationEntity(
                record.getOrganizationId(),
                record.getName(),
                record.getGoogleCalendarId()
        );
    }
}
