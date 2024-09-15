package org.example.scheduleevent.clients.calendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.events.repository.model.EventEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GoogleCalendarClient implements CalendarClient {
    @Override
    public String saveEvent(EventEntity eventEntity, String accessToken, String email) {
        var calendar = calendarClient(accessToken);
        var request = createRequest(eventEntity);
        try {
            var event = calendar.events().insert("primary", request).execute();
            return event.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEvent(String eventId, String accessToken) {
        var calendar = calendarClient(accessToken);
        try {
            calendar.events().delete("primary", eventId).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEventToCalendar(String eventId, String accessToken, String email) {
        var calendar = calendarClient(accessToken);
        try {
            var event = calendar.events().get("primary", eventId).execute();
            if (event.getAttendees() == null) {
                event.setAttendees(List.of());
            }

            var attendee = new EventAttendee().setEmail(email);
            var allAttendees = new ArrayList<>(event.getAttendees());
            allAttendees.add(attendee);
            event.setAttendees(allAttendees);

            calendar.events().patch("primary", eventId, event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeEventFromCalendar(String eventId, String accessToken, String email) {
        var calendar = calendarClient(accessToken);
        try {
            var event = calendar.events().get("primary", eventId).execute();
            event.getAttendees().removeIf(attendee -> attendee.getEmail().equals(email));
            calendar.events().patch("primary", eventId, event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEvent(EventEntity eventEntity, String accessToken) {
        var calendar = calendarClient(accessToken);
        var request = createRequest(eventEntity);
        try {
            calendar.events().update("primary", eventEntity.googleCalendarEventId(), request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Calendar calendarClient(String accessToken) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        final NetHttpTransport HTTP_TRANSPORT;

        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .build();
    }

    private Event createRequest(EventEntity eventEntity) {
        var event = new Event();
        event.setStart(getDateTime(eventEntity.startDate()));
        event.setEnd(getDateTime(eventEntity.endDate()));
        event.setLocation(eventEntity.locationName());
        event.setSummary(eventEntity.name());
        eventEntity.description().ifPresent(event::setDescription);
        event.setAnyoneCanAddSelf(true);
        event.setGuestsCanSeeOtherGuests(true);
        return event;
    }

    private EventDateTime getDateTime(LocalDateTime dateTime) {
        var eventDateTime = new EventDateTime();
        eventDateTime.setDateTime(convertToDateTime(dateTime));
        eventDateTime.setTimeZone("UTC");
        return eventDateTime;
    }

    private DateTime convertToDateTime(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        long epochMilli = zonedDateTime.toInstant().toEpochMilli();
        return new DateTime(epochMilli, zonedDateTime.getOffset().getTotalSeconds() / 60);
    }

}
