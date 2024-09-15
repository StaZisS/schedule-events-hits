package org.example.scheduleevent.rest.controller.user;

import org.example.scheduleevent.core.user.UserEntity;

public class UserMapper {
    public static UserProfile map(UserEntity entity) {
        return new UserProfile(
                entity.id(),
                entity.username(),
                entity.email()
        );
    }
}
