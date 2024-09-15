package org.example.scheduleevent.core.manager.repository;

import org.jooq.Condition;

import static com.example.schedule_event.public_.Tables.APPLICATIONS_FOR_MEMBERSHIP;

public class ApplicationForMembershipProp {
    public static Condition filterByOrganizationId(Long organizationId) {
        return APPLICATIONS_FOR_MEMBERSHIP.ORGANIZATION_ID.eq(organizationId);
    }

    public static Condition filterByManagerId(String managerId) {
        return APPLICATIONS_FOR_MEMBERSHIP.MANAGER_ID.eq(managerId);
    }
}
