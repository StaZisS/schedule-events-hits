package org.example.scheduleevent.core.manager.repository;

import org.example.scheduleevent.public_interface.common.PaginationDto;
import org.jooq.Condition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ManagerRepository {
    void createApplicationForManager(String userId, Long organizationId);
    void addManager(String userId, Long organizationId);
    void approveApplication(Long applicationId);
    void rejectApplication(Long applicationId);
    boolean isManagerFromOrganization(String userId, Long organizationId);
    Optional<ApplicationForMembershipEntity> getApplication(Long applicationId);
    Stream<ApplicationForMembershipEntity> getApplications(List<Condition> conditions, PaginationDto pagination);
    Optional<Long> getManagerOrganizationId(String userId);
}
