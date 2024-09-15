package org.example.scheduleevent.clients.keycloak;

import org.example.scheduleevent.core.user.UserEntity;
import org.example.scheduleevent.public_interface.user.UpdateUserDto;

import java.util.List;
import java.util.Optional;

public interface UserClient {
    String registerUser(UserEntity entity);

    void deleteUser(String oauthId);

    void updateUser(UpdateUserDto dto);

    Optional<UserEntity> getUser(String oauthId);

    Optional<UserEntity> getUserByUsername(String username);

    Optional<UserEntity> getUserByEmail(String email);

    List<UserEntity> getUsersByUsername(String username);

    List<UserEntity> getAllUsers();
}
