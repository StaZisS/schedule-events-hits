package org.example.scheduleevent.rest.controller.user;

import org.example.scheduleevent.core.user.UserEntity;
import org.example.scheduleevent.public_interface.user.UserRoles;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserProfile map(UserEntity entity) {
        return new UserProfile(
                entity.id(),
                entity.username(),
                entity.email(),
                entity.roles().stream()
                        .map(UserRoles::name)
                        .collect(Collectors.toSet())
        );
    }
}
