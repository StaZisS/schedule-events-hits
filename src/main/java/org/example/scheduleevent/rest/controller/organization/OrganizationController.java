package org.example.scheduleevent.rest.controller.organization;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.organization.OrganizationService;
import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.example.scheduleevent.rest.controller.admin.OrganizationMapper;
import org.example.scheduleevent.rest.controller.admin.OrganizationView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
@Tag(name = "Admin")
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
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
