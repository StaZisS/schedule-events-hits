package org.example.scheduleevent.clients.keycloak;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.public_interface.user.UserRoles;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KeycloakRoleClient implements RoleClient {
    private static final Set<String> SERVER_ROLES = Arrays.stream(UserRoles.values()).map(Enum::name).collect(Collectors.toSet());

    private final Keycloak keycloak;
    private final String realm;

    @Override
    public void assignRole(String userId, Set<UserRoles> roles) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        RolesResource rolesResource = getRolesResource();
        List<RoleRepresentation> representations = roles.stream()
                .map(UserRoles::name)
                .map(role -> "ROLE_" + role)
                .map(rolesResource::get)
                .map(RoleResource::toRepresentation)
                .toList();
        userResource.roles().realmLevel().add(representations);
    }

    @Override
    public void removeRole(String userId, String roleName) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        RolesResource rolesResource = getRolesResource();
        RoleRepresentation representation = rolesResource.get("ROLE_" + roleName).toRepresentation();
        userResource.roles().realmLevel().remove(Collections.singletonList(representation));
    }

    @Override
    public Set<UserRoles> getRoles(String userId) {
        return keycloak.realm(realm).users().get(userId).roles().realmLevel().listAll().stream()
                .map(RoleRepresentation::getName)
                .map(name -> name.replace("ROLE_", ""))
                .filter(SERVER_ROLES::contains)
                .map(UserRoles::valueOf)
                .collect(Collectors.toSet());
    }

    private RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }
}
