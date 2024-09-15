package org.example.scheduleevent.core.manager.repository;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.public_interface.manager.ApplicationStatus;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.schedule_event.public_.Tables.APPLICATIONS_FOR_MEMBERSHIP;
import static com.example.schedule_event.public_.Tables.ORGANIZATION_MANAGER;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepository {
    private final DSLContext create;
    private final ApplicationForMembershipEntityMapper applicationForMembershipEntityMapper;

    @Override
    public void createApplicationForManager(String userId, Long organizationId) {
        create.insertInto(APPLICATIONS_FOR_MEMBERSHIP)
                .set(APPLICATIONS_FOR_MEMBERSHIP.MANAGER_ID, userId)
                .set(APPLICATIONS_FOR_MEMBERSHIP.ORGANIZATION_ID, organizationId)
                .set(APPLICATIONS_FOR_MEMBERSHIP.STATUS, ApplicationStatus.PENDING.name())
                .set(APPLICATIONS_FOR_MEMBERSHIP.CREATED_AT, LocalDateTime.now())
                .execute();
    }

    @Override
    public void addManager(String userId, Long organizationId) {
        create.insertInto(ORGANIZATION_MANAGER)
                .set(ORGANIZATION_MANAGER.MANAGER_ID, userId)
                .set(ORGANIZATION_MANAGER.ORGANIZATION_ID, organizationId)
                .execute();
    }

    @Override
    public void approveApplication(Long applicationId) {
        create.update(APPLICATIONS_FOR_MEMBERSHIP)
                .set(APPLICATIONS_FOR_MEMBERSHIP.STATUS, ApplicationStatus.APPROVED.name())
                .where(APPLICATIONS_FOR_MEMBERSHIP.APPLICATION_ID.eq(applicationId))
                .execute();
    }

    @Override
    public void rejectApplication(Long applicationId) {
        create.update(APPLICATIONS_FOR_MEMBERSHIP)
                .set(APPLICATIONS_FOR_MEMBERSHIP.STATUS, ApplicationStatus.REJECTED.name())
                .where(APPLICATIONS_FOR_MEMBERSHIP.APPLICATION_ID.eq(applicationId))
                .execute();
    }

    @Override
    public boolean isManagerFromOrganization(String userId, Long organizationId) {
        return create.fetchExists(
                create.selectFrom(ORGANIZATION_MANAGER)
                        .where(ORGANIZATION_MANAGER.MANAGER_ID.eq(userId))
                        .and(ORGANIZATION_MANAGER.ORGANIZATION_ID.eq(organizationId))
        );
    }

    @Override
    public Optional<ApplicationForMembershipEntity> getApplication(Long applicationId) {
        return create.selectFrom(APPLICATIONS_FOR_MEMBERSHIP)
                .where(APPLICATIONS_FOR_MEMBERSHIP.APPLICATION_ID.eq(applicationId))
                .fetchOptional(applicationForMembershipEntityMapper);
    }

    @Override
    public Stream<ApplicationForMembershipEntity> getApplications(List<Condition> conditions, PaginationDto pagination) {
        var offset = (pagination.page() - 1) * pagination.size();
        var limit = pagination.size();
        return create.selectFrom(APPLICATIONS_FOR_MEMBERSHIP)
                .where(conditions)
                .orderBy(APPLICATIONS_FOR_MEMBERSHIP.CREATED_AT.desc())
                .limit(limit)
                .offset(offset)
                .fetchStream()
                .map(applicationForMembershipEntityMapper);
    }

    @Override
    public Optional<Long> getManagerOrganizationId(String userId) {
        return create.select(ORGANIZATION_MANAGER.ORGANIZATION_ID)
                .from(ORGANIZATION_MANAGER)
                .where(ORGANIZATION_MANAGER.MANAGER_ID.eq(userId))
                .fetchOptionalInto(Long.class);
    }
}
