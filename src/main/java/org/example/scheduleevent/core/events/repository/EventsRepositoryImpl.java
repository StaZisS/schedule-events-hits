package org.example.scheduleevent.core.events.repository;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.events.repository.model.EventEntity;
import org.example.scheduleevent.core.events.repository.model.EventEntityMapper;
import org.example.scheduleevent.core.events.repository.model.UserEventEntity;
import org.example.scheduleevent.core.events.repository.model.UserEventEntityMapper;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.schedule_event.public_.Tables.EVENT;
import static com.example.schedule_event.public_.Tables.USER_EVENT;

@Repository
@RequiredArgsConstructor
public class EventsRepositoryImpl implements EventsRepository {
    private final DSLContext create;
    private final EventEntityMapper eventEntityMapper;
    private final UserEventEntityMapper userEventEntityMapper;

    @Override
    public EventEntity createEvent(EventEntity eventEntity) {
        return create.insertInto(EVENT)
                .set(EVENT.ORGANIZATION_ID, eventEntity.organizationId())
                .set(EVENT.CREATOR_ID, eventEntity.creatorId())
                .set(EVENT.NAME, eventEntity.name())
                .set(EVENT.DESCRIPTION, eventEntity.description().orElse(null))
                .set(EVENT.START_DATE, eventEntity.startDate())
                .set(EVENT.END_DATE, eventEntity.endDate())
                .set(EVENT.DEADLINE, eventEntity.deadline())
                .set(EVENT.LOCATION_NAME, eventEntity.locationName())
                .set(EVENT.GOOGLE_EVENT_ID, eventEntity.googleCalendarEventId())
                .returning(EVENT.EVENT_ID, EVENT.ORGANIZATION_ID, EVENT.CREATOR_ID, EVENT.NAME, EVENT.DESCRIPTION, EVENT.START_DATE, EVENT.END_DATE, EVENT.DEADLINE, EVENT.LOCATION_NAME, EVENT.GOOGLE_EVENT_ID)
                .fetchOne(eventEntityMapper);
    }

    @Override
    public Optional<EventEntity> getEventById(Long eventId) {
        return create.selectFrom(EVENT)
                .where(EVENT.EVENT_ID.eq(eventId))
                .fetchOptional(eventEntityMapper);
    }

    @Override
    public void updateEvent(EventEntity eventEntity) {
        create.update(EVENT)
                .set(EVENT.ORGANIZATION_ID, eventEntity.organizationId())
                .set(EVENT.CREATOR_ID, eventEntity.creatorId())
                .set(EVENT.NAME, eventEntity.name())
                .set(EVENT.DESCRIPTION, eventEntity.description().orElse(null))
                .set(EVENT.START_DATE, eventEntity.startDate())
                .set(EVENT.END_DATE, eventEntity.endDate())
                .set(EVENT.DEADLINE, eventEntity.deadline())
                .set(EVENT.LOCATION_NAME, eventEntity.locationName())
                .set(EVENT.GOOGLE_EVENT_ID, eventEntity.googleCalendarEventId())
                .where(EVENT.EVENT_ID.eq(eventEntity.id()))
                .execute();
    }

    @Override
    public void deleteEvent(Long eventId) {
        create.deleteFrom(EVENT)
                .where(EVENT.EVENT_ID.eq(eventId))
                .execute();
    }

    @Override
    public Stream<EventEntity> getEvents(List<Condition> conditions, PaginationDto pagination) {
        int limit = pagination.size();
        int offset = (pagination.page() - 1) * pagination.size();
        return create.selectFrom(EVENT)
                .where(conditions)
                .limit(limit)
                .offset(offset)
                .fetchStream()
                .map(eventEntityMapper);
    }

    @Override
    public Stream<EventEntity> getUserEvents(String userId) {
        return create.select(EVENT.fields())
                .from(USER_EVENT)
                .join(EVENT)
                .on(USER_EVENT.EVENT_ID.eq(EVENT.EVENT_ID))
                .where(USER_EVENT.USER_ID.eq(userId))
                .fetchStream()
                .map(record -> eventEntityMapper.apply(record.into(EVENT)));
    }

    @Override
    public Stream<String> getUsersIdsForEvent(Long eventId) {
        return create.select(USER_EVENT.USER_ID)
                .from(USER_EVENT)
                .where(USER_EVENT.EVENT_ID.eq(eventId))
                .fetchStream()
                .map(record -> record.get(USER_EVENT.USER_ID));
    }

    @Override
    public Optional<UserEventEntity> getEventUser(Long eventId, String userId) {
        return create.selectFrom(USER_EVENT)
                .where(USER_EVENT.EVENT_ID.eq(eventId).and(USER_EVENT.USER_ID.eq(userId)))
                .fetchOptional(userEventEntityMapper);
    }

    @Override
    public void addUserToEvent(Long eventId, String userId) {
        create.insertInto(USER_EVENT)
                .set(USER_EVENT.EVENT_ID, eventId)
                .set(USER_EVENT.USER_ID, userId)
                .execute();
    }

    @Override
    public void deleteUserFromEvent(Long eventId, String userId) {
        create.deleteFrom(USER_EVENT)
                .where(USER_EVENT.EVENT_ID.eq(eventId).and(USER_EVENT.USER_ID.eq(userId)))
                .execute();
    }
}
