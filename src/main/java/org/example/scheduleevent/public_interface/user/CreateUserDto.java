package org.example.scheduleevent.public_interface.user;

import java.util.Set;

public record CreateUserDto(
        String username,
        String email,
        String password,
        Set<UserRoles> roles
) {
}
