package org.example.scheduleevent.clients.keycloak;

import org.example.scheduleevent.public_interface.user.UserRoles;

import java.util.Set;

public interface RoleClient {
    void assignRole(String userId, Set<UserRoles> roles);

    void removeRole(String userId, String roleName);
}
