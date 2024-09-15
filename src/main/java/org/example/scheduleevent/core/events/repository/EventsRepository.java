package org.example.scheduleevent.core.events.repository;


import org.example.scheduleevent.core.events.repository.model.EventEntity;
import org.example.scheduleevent.core.events.repository.model.UserEventEntity;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.jooq.Condition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface EventsRepository {
    EventEntity createEvent(EventEntity eventEntity);
    Optional<EventEntity> getEventById(Long eventId);
    void updateEvent(EventEntity eventEntity);
    void deleteEvent(Long eventId);
    Stream<EventEntity> getEvents(List<Condition> conditions, PaginationDto pagination);
    Stream<EventEntity> getUserEvents(String userId);
    Stream<String> getUsersIdsForEvent(Long eventId);
    Optional<UserEventEntity> getEventUser(Long eventId, String userId);
    void addUserToEvent(Long eventId, String userId);
    void deleteUserFromEvent(Long eventId, String userId);
}
