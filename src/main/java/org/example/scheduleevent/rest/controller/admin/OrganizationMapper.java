package org.example.scheduleevent.rest.controller.admin;

import org.example.scheduleevent.core.organization.repository.OrganizationEntity;

public class OrganizationMapper {
    public static OrganizationView map(OrganizationEntity entity) {
        return new OrganizationView(
                entity.id(),
                entity.name()
        );
    }
}
