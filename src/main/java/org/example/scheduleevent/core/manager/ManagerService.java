package org.example.scheduleevent.core.manager;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.clients.keycloak.RoleClient;
import org.example.scheduleevent.core.events.EventsService;
import org.example.scheduleevent.core.events.repository.EventsRepository;
import org.example.scheduleevent.core.events.repository.model.EventEntity;
import org.example.scheduleevent.core.manager.repository.ManagerRepository;
import org.example.scheduleevent.core.organization.repository.OrganizationRepository;
import org.example.scheduleevent.core.user.UserEntity;
import org.example.scheduleevent.core.user.UserService;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.public_interface.event.CreateEventDto;
import org.example.scheduleevent.public_interface.exception.ExceptionInApplication;
import org.example.scheduleevent.public_interface.exception.ExceptionType;
import org.example.scheduleevent.public_interface.manager.ApplicationDto;
import org.example.scheduleevent.public_interface.manager.CreateManagerDto;
import org.example.scheduleevent.public_interface.user.UserInfoDto;
import org.example.scheduleevent.public_interface.user.UserRoles;
import org.jooq.Condition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.example.scheduleevent.core.events.repository.EventFilterProperties.filterByCreatorId;
import static org.example.scheduleevent.core.manager.repository.ApplicationForMembershipProp.filterByOrganizationId;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final EventsService eventsService;
    private final EventsRepository eventsRepository;
    private final RoleClient roleClient;
    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    @Transactional
    public String createManager(CreateManagerDto dto) {
        managerRepository.createApplicationForManager(dto.userInfo().userId(), dto.organizationId());

        roleClient.assignRole(dto.userInfo().userId(), Set.of(UserRoles.MANAGER, UserRoles.NOT_CONFIRMED));

        return dto.userInfo().userId();
    }

    @Transactional
    public void approveApplication(Long applicationId, UserInfoDto userInfo) {
        var application = managerRepository.getApplication(applicationId)
                .orElseThrow(() -> new ExceptionInApplication("Application not found", ExceptionType.NOT_FOUND));

        if (!userInfo.roles().contains(UserRoles.ADMIN) && !managerRepository.isManagerFromOrganization(userInfo.userId(), application.organizationId())) {
            throw new ExceptionInApplication("User is not a manager of this organization", ExceptionType.FORBIDDEN);
        }

        roleClient.removeRole(application.managerId(), UserRoles.NOT_CONFIRMED.name());
        managerRepository.addManager(application.managerId(), application.organizationId());
        managerRepository.approveApplication(applicationId);
    }

    @Transactional
    public void rejectApplication(Long applicationId, UserInfoDto userInfo) {
        var application = managerRepository.getApplication(applicationId)
                .orElseThrow(() -> new ExceptionInApplication("Application not found", ExceptionType.NOT_FOUND));

        if (!userInfo.roles().contains(UserRoles.ADMIN) && !managerRepository.isManagerFromOrganization(userInfo.userId(), application.organizationId())) {
            throw new ExceptionInApplication("User is not a manager of this organization", ExceptionType.FORBIDDEN);
        }

        managerRepository.rejectApplication(applicationId);
    }

    public List<UserEntity> getUsersFromEvent(Long eventId, UserInfoDto userInfo) {
        var event = eventsService.getEventById(eventId);

        if (!userInfo.roles().contains(UserRoles.ADMIN) && !managerRepository.isManagerFromOrganization(userInfo.userId(), event.organizationId())) {
            throw new ExceptionInApplication("User is not a manager of this organization who created the event", ExceptionType.FORBIDDEN);
        }

        return eventsService.getUsersFromEvent(eventId);
    }

    public EventEntity createEvent(CreateEventDto dto, UserInfoDto userInfo) {
        if (userInfo.roles().contains(UserRoles.MANAGER)) {
            var managerOrganizationId = managerRepository.getManagerOrganizationId(userInfo.userId())
                    .orElseThrow(() -> new ExceptionInApplication("User is not a manager", ExceptionType.FORBIDDEN));
            var updatedDto = new CreateEventDto(
                    dto.creatorId(),
                    dto.name(),
                    dto.description(),
                    dto.location(),
                    dto.startDateTime(),
                    dto.endDateTime(),
                    dto.deadlineDateTime(),
                    Optional.of(managerOrganizationId)
            );
            return eventsService.createEvent(updatedDto, userInfo);
        } else if (userInfo.roles().contains(UserRoles.ADMIN)) {
            return eventsService.createEvent(dto, userInfo);
        } else {
            throw new ExceptionInApplication("User is not a manager of this organization", ExceptionType.FORBIDDEN);
        }
    }

    public EventEntity updateEvent(Long eventId, CreateEventDto dto, UserInfoDto userInfo) {
        var event = eventsService.getEventById(eventId);
        if (userInfo.roles().contains(UserRoles.MANAGER)) {
            var managerOrganizationId = managerRepository.getManagerOrganizationId(userInfo.userId())
                    .orElseThrow(() -> new ExceptionInApplication("User is not a manager", ExceptionType.FORBIDDEN));
            if (!managerOrganizationId.equals(event.organizationId()) || !event.creatorId().equals(userInfo.userId())) {
                throw new ExceptionInApplication("User is not a manager of this organization who created the event", ExceptionType.FORBIDDEN);
            }
            var updatedDto = new CreateEventDto(
                    event.creatorId(),
                    dto.name(),
                    dto.description(),
                    dto.location(),
                    dto.startDateTime(),
                    dto.endDateTime(),
                    dto.deadlineDateTime(),
                    Optional.of(managerOrganizationId)
            );
            return eventsService.updateEvent(eventId, updatedDto, userInfo);
        } else if (userInfo.roles().contains(UserRoles.ADMIN)) {
            return eventsService.updateEvent(eventId, dto, userInfo);
        } else {
            throw new ExceptionInApplication("User is not a manager of this organization who created the event", ExceptionType.FORBIDDEN);
        }
    }

    public void deleteEvent(Long eventId, UserInfoDto userInfo) {
        var event = eventsService.getEventById(eventId);
        if (userInfo.roles().contains(UserRoles.MANAGER)) {
            var managerOrganizationId = managerRepository.getManagerOrganizationId(userInfo.userId())
                    .orElseThrow(() -> new ExceptionInApplication("User is not a manager", ExceptionType.FORBIDDEN));
            if (!managerOrganizationId.equals(event.organizationId()) || !event.creatorId().equals(userInfo.userId())) {
                throw new ExceptionInApplication("User is not a manager of this organization who created the event", ExceptionType.FORBIDDEN);
            }
            eventsService.deleteEvent(eventId, userInfo);
        } else if (userInfo.roles().contains(UserRoles.ADMIN)) {
            eventsService.deleteEvent(eventId, userInfo);
        } else {
            throw new ExceptionInApplication("User is not a manager of this organization who created the event", ExceptionType.FORBIDDEN);
        }
    }

    public List<EventEntity> getMyEvents(UserInfoDto userInfo, PaginationDto pagination) {
        return eventsRepository.getEvents(List.of(filterByCreatorId(userInfo.userId())), pagination).toList();
    }

    public List<ApplicationDto> getApplications(UserInfoDto userInfo, PaginationDto pagination) {
        var filterOptions = new ArrayList<Condition>();
        if (!userInfo.roles().contains(UserRoles.ADMIN)) {
            var managerOrganizationId = managerRepository.getManagerOrganizationId(userInfo.userId())
                    .orElseThrow(() -> new ExceptionInApplication("User is not a manager", ExceptionType.FORBIDDEN));
            filterOptions.add(filterByOrganizationId(managerOrganizationId));
        }
        var applications = managerRepository.getApplications(filterOptions, pagination);

        return applications
                .parallel()
                .map(application -> new ApplicationDto(
                        application,
                        userService.getProfile(application.managerId()),
                        organizationRepository.getOrganizationById(application.organizationId())
                ))
                .toList();
    }
}
