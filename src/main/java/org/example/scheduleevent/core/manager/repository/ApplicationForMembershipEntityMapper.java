package org.example.scheduleevent.core.manager.repository;

import com.example.schedule_event.public_.tables.records.ApplicationsForMembershipRecord;
import org.example.scheduleevent.public_interface.manager.ApplicationStatus;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationForMembershipEntityMapper implements RecordMapper<ApplicationsForMembershipRecord, ApplicationForMembershipEntity> {
    @Override
    public ApplicationForMembershipEntity map(ApplicationsForMembershipRecord applicationsForMembershipRecord) {
        return new ApplicationForMembershipEntity(
                applicationsForMembershipRecord.getApplicationId(),
                applicationsForMembershipRecord.getOrganizationId(),
                applicationsForMembershipRecord.getManagerId(),
                ApplicationStatus.valueOf(applicationsForMembershipRecord.getStatus()),
                applicationsForMembershipRecord.getCreatedAt()
        );
    }
}
