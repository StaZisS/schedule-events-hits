package org.example.scheduleevent.core.user;

import org.example.scheduleevent.public_interface.user.UserRoles;

import java.util.Set;

public record UserEntity(
        String id,
        String username,
        String email,
        String password,
        Set<UserRoles> roles
) {
}
