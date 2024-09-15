package org.example.scheduleevent.clients.calendar;

import org.example.scheduleevent.core.events.repository.model.EventEntity;

public interface CalendarClient {
    String saveEvent(EventEntity eventEntity, String accessToken, String calendarId);
    void deleteEvent(String eventId, String accessToken, String calendarId);
    void addEventToCalendar(String eventId, String accessToken, String email, String calendarId);
    void removeEventFromCalendar(String eventId, String accessToken, String email, String calendarId);
    void updateEvent(EventEntity eventEntity, String accessToken, String calendarId);
    String createCalendar(String name, String accessToken);
    void deleteCalendar(String calendarId, String accessToken);
    void updateCalendar(String calendarId, String name, String accessToken);
}
