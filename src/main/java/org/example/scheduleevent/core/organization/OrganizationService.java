package org.example.scheduleevent.core.organization;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.organization.repository.OrganizationEntity;
import org.example.scheduleevent.core.organization.repository.OrganizationRepository;
import org.example.scheduleevent.public_interface.admin.CreateOrganizationDto;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationEntity createOrganization(CreateOrganizationDto dto) {
        var entity = new OrganizationEntity(
                null,
                dto.name()
        );
        return organizationRepository.createOrganization(entity);
    }

    @Transactional
    public void deleteOrganization(Long organizationId) {
        organizationRepository.deleteOrganization(organizationId);
    }

    public OrganizationEntity updateOrganization(Long organizationId, CreateOrganizationDto dto) {
        var entity = new OrganizationEntity(
                organizationId,
                dto.name()
        );
        organizationRepository.updateOrganization(entity);
        return entity;
    }

    @Transactional(readOnly = true)
    public List<OrganizationEntity> getOrganizations(PaginationDto dto) {
        return organizationRepository.getOrganizations(dto).toList();
    }

}
