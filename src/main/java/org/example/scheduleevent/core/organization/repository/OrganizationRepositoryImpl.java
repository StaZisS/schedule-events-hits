package org.example.scheduleevent.core.organization.repository;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.schedule_event.public_.Tables.ORGANIZATION;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {
    private final DSLContext create;
    private final OrganizationEntityMapper organizationEntityMapper;

    @Override
    public OrganizationEntity createOrganization(OrganizationEntity organizationEntity) {
        return create.insertInto(ORGANIZATION)
                .set(ORGANIZATION.NAME, organizationEntity.name())
                .set(ORGANIZATION.GOOGLE_CALENDAR_ID, organizationEntity.googleCalendarId())
                .returning(ORGANIZATION.ORGANIZATION_ID, ORGANIZATION.NAME, ORGANIZATION.GOOGLE_CALENDAR_ID)
                .fetchOne(organizationEntityMapper);
    }

    @Override
    public Optional<OrganizationEntity> getOrganizationById(Long organizationId) {
        return create.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.ORGANIZATION_ID.eq(organizationId))
                .fetchOptional(organizationEntityMapper);
    }

    @Override
    public void updateOrganization(OrganizationEntity organizationEntity) {
        create.update(ORGANIZATION)
                .set(ORGANIZATION.NAME, organizationEntity.name())
                .set(ORGANIZATION.GOOGLE_CALENDAR_ID, organizationEntity.googleCalendarId())
                .where(ORGANIZATION.ORGANIZATION_ID.eq(organizationEntity.id()))
                .execute();
    }

    @Override
    public void deleteOrganization(Long organizationId) {
        create.deleteFrom(ORGANIZATION)
                .where(ORGANIZATION.ORGANIZATION_ID.eq(organizationId))
                .execute();
    }

    @Override
    public Stream<OrganizationEntity> getOrganizations(PaginationDto pagination) {
        int limit = pagination.size();
        int offset = (pagination.page() - 1) * pagination.size();
        return create.selectFrom(ORGANIZATION)
                .limit(limit)
                .offset(offset)
                .fetchStream()
                .map(organizationEntityMapper);
    }
}
