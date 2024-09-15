package org.example.scheduleevent.core.admin;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.organization.OrganizationService;
import org.example.scheduleevent.core.organization.repository.OrganizationEntity;
import org.example.scheduleevent.public_interface.admin.CreateOrganizationDto;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final OrganizationService organizationService;

    @Transactional
    public OrganizationEntity createOrganization(CreateOrganizationDto dto) {
        return organizationService.createOrganization(dto);
    }


}
