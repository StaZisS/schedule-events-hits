package org.example.scheduleevent.public_interface.manager;

import org.example.scheduleevent.core.manager.repository.ApplicationForMembershipEntity;
import org.example.scheduleevent.core.organization.repository.OrganizationEntity;
import org.example.scheduleevent.core.user.UserEntity;

public record ApplicationDto(
        ApplicationForMembershipEntity application,
        UserEntity user,
        OrganizationEntity organization
) {
}
