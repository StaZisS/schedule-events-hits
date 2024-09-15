package org.example.scheduleevent.core.organization;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.clients.calendar.CalendarClient;
import org.example.scheduleevent.config.auth.GoogleTokenExchange;
import org.example.scheduleevent.core.organization.repository.OrganizationEntity;
import org.example.scheduleevent.core.organization.repository.OrganizationRepository;
import org.example.scheduleevent.public_interface.admin.CreateOrganizationDto;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.public_interface.exception.ExceptionInApplication;
import org.example.scheduleevent.public_interface.exception.ExceptionType;
import org.example.scheduleevent.public_interface.user.UserInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final CalendarClient calendarClient;
    private final GoogleTokenExchange googleTokenExchange;

    @Transactional
    public OrganizationEntity createOrganization(CreateOrganizationDto dto, UserInfoDto userInfo) {
        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());
        var googleCalendarId = calendarClient.createCalendar(dto.name(), googleToken);
        var entity = new OrganizationEntity(
                null,
                dto.name(),
                googleCalendarId
        );
        return organizationRepository.createOrganization(entity);
    }

    @Transactional
    public void deleteOrganization(Long organizationId, UserInfoDto userInfo) {
        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());
        var organization = organizationRepository.getOrganizationById(organizationId)
                .orElseThrow(() -> new ExceptionInApplication("Organization not found", ExceptionType.NOT_FOUND));

        calendarClient.deleteCalendar(organization.googleCalendarId(), googleToken);
        organizationRepository.deleteOrganization(organizationId);
    }

    public OrganizationEntity updateOrganization(Long organizationId, CreateOrganizationDto dto, UserInfoDto userInfo) {
        var organizationInDb = organizationRepository.getOrganizationById(organizationId)
                .orElseThrow(() -> new ExceptionInApplication("Organization not found", ExceptionType.NOT_FOUND));
        var entity = new OrganizationEntity(
                organizationId,
                dto.name(),
                organizationInDb.googleCalendarId()
        );

        var googleToken = googleTokenExchange.exchangeToken(userInfo.accessToken());

        calendarClient.updateCalendar(entity.googleCalendarId(), dto.name(), googleToken);
        organizationRepository.updateOrganization(entity);
        return entity;
    }

    @Transactional(readOnly = true)
    public List<OrganizationEntity> getOrganizations(PaginationDto dto) {
        return organizationRepository.getOrganizations(dto).toList();
    }

}
