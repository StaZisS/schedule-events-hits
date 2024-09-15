package org.example.scheduleevent.rest.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.admin.AdminService;
import org.example.scheduleevent.core.organization.OrganizationService;
import org.example.scheduleevent.public_interface.admin.CreateOrganizationDto;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.example.scheduleevent.rest.controller.admin.OrganizationMapper.map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final OrganizationService organizationService;

    @PostMapping(path = "/create_organization")
    @SecurityRequirement(name = "oauth2")
    public OrganizationView createOrganization(@RequestBody CreateOrganizationRequest request) {
        var dto = new CreateOrganizationDto(
                request.name()
        );

        var entity = adminService.createOrganization(dto);
        return map(entity);
    }

    @DeleteMapping(path = "/organization/{organizationId}")
    @SecurityRequirement(name = "oauth2")
    public void deleteOrganization(@PathVariable Long organizationId) {
        organizationService.deleteOrganization(organizationId);
    }

    @PostMapping(path = "/organization/{organizationId}")
    @SecurityRequirement(name = "oauth2")
    public OrganizationView updateOrganization(@PathVariable Long organizationId, @RequestBody CreateOrganizationRequest request) {
        var dto = new CreateOrganizationDto(
                request.name()
        );

        var entity = organizationService.updateOrganization(organizationId, dto);
        return map(entity);
    }

    @GetMapping(path = "/organizations")
    @SecurityRequirement(name = "oauth2")
    public List<OrganizationView> getOrganizations(@RequestParam Optional<Integer> page,
                                                   @RequestParam Optional<Integer> size) {
        var paginationDto = new PaginationDto(
                page.orElse(1),
                size.orElse(10)
        );
        return organizationService.getOrganizations(paginationDto).stream()
                .map(OrganizationMapper::map)
                .toList();
    }

}
