package org.example.scheduleevent.rest.controller.events;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.events.EventsService;
import org.example.scheduleevent.core.manager.ManagerService;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.public_interface.event.CreateEventDto;
import org.example.scheduleevent.public_interface.event.GetEventsDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.scheduleevent.rest.controller.events.EventMapper.map;
import static org.example.scheduleevent.rest.util.JwtTokenUtil.getUserInfoFromToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Tag(name = "Event")
public class EventController {
    private final ManagerService managerService;
    private final EventsService eventsService;

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public EventView createEvent(@RequestBody CreateEventRequest request, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        var createDto = new CreateEventDto(
                userInfo.userId(),
                request.name(),
                request.description(),
                request.location(),
                request.startDateTime(),
                request.endDateTime(),
                request.deadlineDateTime(),
                request.organizationId()
        );
        var entity = managerService.createEvent(createDto, userInfo);
        return map(entity);
    }

    @PostMapping(path = "/{eventId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public EventView updateEvent(@PathVariable Long eventId, @RequestBody CreateEventRequest request, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        var createDto = new CreateEventDto(
                userInfo.userId(),
                request.name(),
                request.description(),
                request.location(),
                request.startDateTime(),
                request.endDateTime(),
                request.deadlineDateTime(),
                request.organizationId()
        );
        var entity = managerService.updateEvent(eventId, createDto, userInfo);
        return map(entity);
    }

    @DeleteMapping(path = "/{eventId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public void deleteEvent(@PathVariable Long eventId, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        managerService.deleteEvent(eventId, userInfo);
    }

    @GetMapping(path = "/my")
    @SecurityRequirement(name = "oauth2")
    public List<EventView> getMyEvents(JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        return eventsService.getUserEvents(userInfo.userId())
                .stream()
                .map(EventMapper::map)
                .toList();
    }

    @GetMapping
    @SecurityRequirement(name = "oauth2")
    public List<EventView> getAllEvents(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> location,
            @RequestParam Optional<LocalDateTime> startDateTime,
            @RequestParam Optional<LocalDateTime> endDateTime,
            @RequestParam Optional<Long> organizationId
    ) {
        var dto = new GetEventsDto(
                organizationId,
                startDateTime,
                endDateTime,
                location,
                Optional.of(new PaginationDto(
                        page.orElse(1),
                        size.orElse(10)
                ))
        );
        return eventsService.getEvents(dto)
                .stream()
                .map(EventMapper::map)
                .toList();
    }

    @PostMapping(path = "/{eventId}/add")
    @SecurityRequirement(name = "oauth2")
    public void subscribeToEvent(@PathVariable Long eventId, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        eventsService.subscribeToEvent(eventId, userInfo);
    }

    @PostMapping(path = "/{eventId}/remove")
    @SecurityRequirement(name = "oauth2")
    public void unsubscribeFromEvent(@PathVariable Long eventId, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        eventsService.unsubscribeFromEvent(eventId, userInfo);
    }

}
