package org.example.scheduleevent.clients.calendar;

import org.example.scheduleevent.core.events.repository.model.EventEntity;

public interface CalendarClient {
    String saveEvent(EventEntity eventEntity, String accessToken, String email);
    void deleteEvent(String eventId, String accessToken);
    void addEventToCalendar(String eventId, String accessToken, String email);
    void removeEventFromCalendar(String eventId, String accessToken, String email);
    void updateEvent(EventEntity eventEntity, String accessToken);
}
