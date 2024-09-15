package org.example.scheduleevent.core.organization.repository;

import org.example.scheduleevent.public_interface.common.PaginationDto;

import java.util.Optional;
import java.util.stream.Stream;

public interface OrganizationRepository {
    OrganizationEntity createOrganization(OrganizationEntity organizationEntity);
    Optional<OrganizationEntity> getOrganizationById(Long organizationId);
    void updateOrganization(OrganizationEntity organizationEntity);
    void deleteOrganization(Long organizationId);
    Stream<OrganizationEntity> getOrganizations(PaginationDto pagination);
}
