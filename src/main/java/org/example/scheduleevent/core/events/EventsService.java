package org.example.scheduleevent.core.events;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.clients.calendar.CalendarClient;
import org.example.scheduleevent.config.auth.GoogleTokenExchange;
import org.example.scheduleevent.core.events.repository.model.EventEntity;
import org.example.scheduleevent.core.events.repository.EventsRepository;
import org.example.scheduleevent.core.user.UserEntity;
import org.example.scheduleevent.core.user.UserService;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.public_interface.event.CreateEventDto;
import org.example.scheduleevent.public_interface.event.GetEventsDto;
import org.example.scheduleevent.public_interface.exception.ExceptionInApplication;
import org.example.scheduleevent.public_interface.exception.ExceptionType;
import org.example.scheduleevent.public_interface.user.UserInfoDto;
import org.jooq.Condition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.scheduleevent.core.events.repository.EventFilterProperties.filterByLeftBoundaryDate;
import static org.example.scheduleevent.core.events.repository.EventFilterProperties.filterByLocation;
import static org.example.scheduleevent.core.events.repository.EventFilterProperties.filterByOrganizationId;
import static org.example.scheduleevent.core.events.repository.EventFilterProperties.filterByRightBoundaryDate;

@Service
@RequiredArgsConstructor
public class EventsService {
    private final EventsRepository eventsRepository;
    private final UserService userService;
    private final CalendarClient calendarClient;
    private final GoogleTokenExchange googleTokenExchange;

    @Transactional
    public EventEntity createEvent(CreateEventDto dto, UserInfoDto userInfo) {
        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());
        var entity = new EventEntity(
                null,
                dto.organizationId().orElseThrow(),
                dto.creatorId(),
                dto.name(),
                dto.description(),
                dto.startDateTime(),
                dto.endDateTime(),
                dto.deadlineDateTime(),
                dto.location(),
                null
        );

        var googleCalendarEventId = calendarClient.saveEvent(entity, googleToken, userInfo.email());
        var eventWithGoogleId = new EventEntity(
                null,
                dto.organizationId().orElseThrow(),
                dto.creatorId(),
                dto.name(),
                dto.description(),
                dto.startDateTime(),
                dto.endDateTime(),
                dto.deadlineDateTime(),
                dto.location(),
                googleCalendarEventId
        );

        return eventsRepository.createEvent(eventWithGoogleId);
    }

    @Transactional
    public EventEntity updateEvent(Long eventId, CreateEventDto dto, UserInfoDto userInfo) {
        var eventInDb = eventsRepository.getEventById(eventId)
                .orElseThrow(() -> new ExceptionInApplication("Event not found", ExceptionType.NOT_FOUND));
        var entity = new EventEntity(
                eventId,
                dto.organizationId().orElseThrow(),
                dto.creatorId(),
                dto.name(),
                dto.description(),
                dto.startDateTime(),
                dto.endDateTime(),
                dto.deadlineDateTime(),
                dto.location(),
                eventInDb.googleCalendarEventId()
        );

        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());
        calendarClient.updateEvent(entity, googleToken);
        eventsRepository.updateEvent(entity);
        return entity;
    }

    @Transactional
    public void deleteEvent(Long eventId, UserInfoDto userInfo) {
        var event = eventsRepository.getEventById(eventId)
                .orElseThrow(() -> new ExceptionInApplication("Event not found", ExceptionType.NOT_FOUND));

        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());
        calendarClient.deleteEvent(event.googleCalendarEventId(), googleToken);
        eventsRepository.deleteEvent(eventId);
    }

    @Transactional(readOnly = true)
    public EventEntity getEventById(Long eventId) {
        return eventsRepository.getEventById(eventId)
                .orElseThrow(() -> new ExceptionInApplication("Event not found", ExceptionType.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<EventEntity> getEvents(GetEventsDto dto) {
        var filterProp = new ArrayList<Condition>();

        dto.location().ifPresent(location -> filterProp.add(filterByLocation(location)));
        dto.endDateTime().ifPresent(endDateTime -> filterProp.add(filterByRightBoundaryDate(endDateTime)));
        dto.startDateTime().ifPresent(startDateTime -> filterProp.add(filterByLeftBoundaryDate(startDateTime)));
        dto.organizationId().ifPresent(organizationId -> filterProp.add(filterByOrganizationId(organizationId)));

        var pagination = dto.pagination().orElse(new PaginationDto(0, 10));

        return eventsRepository.getEvents(filterProp, pagination).toList();
    }

    @Transactional(readOnly = true)
    public List<EventEntity> getUserEvents(String userId) {
        return eventsRepository.getUserEvents(userId).toList();
    }

    public List<UserEntity> getUsersFromEvent(Long eventId) {
        return eventsRepository.getUsersIdsForEvent(eventId)
                .parallel()
                .map(userService::getProfile)
                .toList();
    }

    @Transactional
    public void subscribeToEvent(Long eventId, UserInfoDto userInfo) {
        eventsRepository.getEventUser(eventId, userInfo.userId())
                .ifPresent(userEventEntity -> {
                    throw new ExceptionInApplication("User already subscribed to event", ExceptionType.INVALID);
                });
        var event = eventsRepository.getEventById(eventId)
                .orElseThrow(() -> new ExceptionInApplication("Event not found", ExceptionType.NOT_FOUND));

        if (event.deadline().isBefore(LocalDateTime.now())) {
            throw new ExceptionInApplication("Event deadline is before start date", ExceptionType.INVALID);
        }

        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());

        calendarClient.addEventToCalendar(event.googleCalendarEventId(), googleToken, userInfo.email());
        eventsRepository.addUserToEvent(eventId, userInfo.userId());
    }

    @Transactional
    public void unsubscribeFromEvent(Long eventId, UserInfoDto userInfo) {
        eventsRepository.getEventUser(eventId, userInfo.userId())
                .orElseThrow(() -> new ExceptionInApplication("User not subscribed to event", ExceptionType.INVALID));
        var event = eventsRepository.getEventById(eventId)
                .orElseThrow(() -> new ExceptionInApplication("Event not found", ExceptionType.NOT_FOUND));

        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());

        calendarClient.removeEventFromCalendar(event.googleCalendarEventId(), googleToken, userInfo.email());
        eventsRepository.deleteUserFromEvent(eventId, userInfo.userId());
    }
}
