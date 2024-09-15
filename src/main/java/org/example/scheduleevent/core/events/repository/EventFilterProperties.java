package org.example.scheduleevent.core.events.repository;

import org.jooq.Condition;

import java.time.LocalDateTime;

import static com.example.schedule_event.public_.tables.Event.EVENT;

public class EventFilterProperties {
    public static Condition filterByOrganizationId(Long organizationId) {
        return EVENT.ORGANIZATION_ID.eq(organizationId);
    }

    public static Condition filterByCreatorId(String creatorId) {
        return EVENT.CREATOR_ID.eq(creatorId);
    }

    public static Condition filterByLeftBoundaryDate(LocalDateTime leftBoundaryStartDate) {
        return EVENT.START_DATE.ge(leftBoundaryStartDate);
    }

    public static Condition filterByRightBoundaryDate(LocalDateTime rightBoundaryStartDate) {
        return EVENT.END_DATE.le(rightBoundaryStartDate);
    }

    public static Condition filterByLocation(String location) {
        return EVENT.LOCATION_NAME.contains(location);
    }
}
