package org.example.scheduleevent.rest.controller.user;

import java.util.Set;

public record UserProfile(
        String id,
        String username,
        String email,
        Set<String> roles
) {
}
