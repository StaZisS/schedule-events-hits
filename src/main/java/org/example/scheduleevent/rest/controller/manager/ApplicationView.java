package org.example.scheduleevent.rest.controller.manager;

import org.example.scheduleevent.core.manager.repository.ApplicationForMembershipEntity;
import org.example.scheduleevent.core.organization.repository.OrganizationEntity;
import org.example.scheduleevent.core.user.UserEntity;

public record ApplicationView(
        ApplicationForMembershipEntity application,
        UserEntity user,
        OrganizationEntity organization
) {
}
