package org.example.scheduleevent.public_interface.user;

import java.util.Set;

public record UserInfoDto(
        String userId,
        Set<UserRoles> roles,
        String accessToken,
        String email
) {
}
