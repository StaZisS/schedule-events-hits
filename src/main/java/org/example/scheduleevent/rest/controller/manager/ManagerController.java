package org.example.scheduleevent.rest.controller.manager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.manager.ManagerService;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.public_interface.manager.CreateManagerDto;
import org.example.scheduleevent.rest.controller.events.EventMapper;
import org.example.scheduleevent.rest.controller.events.EventView;
import org.example.scheduleevent.rest.controller.user.UserMapper;
import org.example.scheduleevent.rest.controller.user.UserProfile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.example.scheduleevent.rest.util.JwtTokenUtil.getUserInfoFromToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
@Tag(name = "Manager")
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping(path = "/register")
    @SecurityRequirement(name = "oauth2")
    public String createUser(@RequestBody CreateManagerRequest request,
                             JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        var dto = new CreateManagerDto(
                request.companyId(),
                userInfo
        );

        return managerService.createManager(dto);
    }

    @PostMapping(path = "/application/{applicationId}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public void approveApplication(@PathVariable Long applicationId, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        managerService.approveApplication(applicationId, userInfo);
    }

    @GetMapping(path = "/application")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public List<ApplicationView> getApplications(
            JwtAuthenticationToken token,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        var userInfo = getUserInfoFromToken(token);
        var pagination = new PaginationDto(
                page.orElse(1),
                size.orElse(10)
        );
        return managerService.getApplications(userInfo, pagination)
                .stream()
                .map(application -> new ApplicationView(
                        application.application(),
                        application.user(),
                        application.organization()
                ))
                .toList();
    }

    @PostMapping(path = "/application/{applicationId}/reject")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public void rejectApplication(@PathVariable Long applicationId, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        managerService.rejectApplication(applicationId, userInfo);
    }

    @GetMapping(path = "/event/{eventId}/users")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public List<UserProfile> getUsersByApplication(@PathVariable Long eventId, JwtAuthenticationToken token) {
        var userInfo = getUserInfoFromToken(token);
        return managerService.getUsersFromEvent(eventId, userInfo)
                .stream()
                .map(UserMapper::map)
                .toList();
    }

    @GetMapping(path = "/event/my")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "oauth2")
    public List<EventView> getMyEvents(
            JwtAuthenticationToken token,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        var userInfo = getUserInfoFromToken(token);
        var pagination = new PaginationDto(
                page.orElse(1),
                size.orElse(10)
        );
        return managerService.getMyEvents(userInfo, pagination)
                .stream()
                .map(EventMapper::map)
                .toList();
    }
}
