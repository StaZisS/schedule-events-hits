package org.example.scheduleevent.core.user;

import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.clients.keycloak.RoleClient;
import org.example.scheduleevent.clients.keycloak.UserClient;
import org.example.scheduleevent.public_interface.exception.ExceptionInApplication;
import org.example.scheduleevent.public_interface.exception.ExceptionType;
import org.example.scheduleevent.public_interface.user.CreateUserDto;
import org.example.scheduleevent.public_interface.user.UpdateUserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    private final RoleClient roleClient;

    public String createUser(CreateUserDto dto) {
        checkUserWithUsernameExists(dto.username());
        checkUserWithEmailExists(dto.email());

        var userEntity = new UserEntity(
                null,
                dto.username(),
                dto.email(),
                dto.password(),
                null
        );
        var oauthId = userClient.registerUser(userEntity);
        roleClient.assignRole(oauthId, dto.roles());
        return oauthId;
    }

    public UserEntity getProfile(String userId) {
        return userClient.getUser(userId)
                .orElseThrow(() -> new ExceptionInApplication("User with this id does not exist", ExceptionType.NOT_FOUND));
    }

    public void updateUser(UpdateUserDto dto) {
        dto.email().ifPresent(this::checkUserWithEmailExists);
        dto.username().ifPresent(this::checkUserWithUsernameExists);
        userClient.updateUser(dto);
    }

    private void checkUserWithUsernameExists(String username) {
        if (userClient.getUserByUsername(username).isPresent()) {
            throw new ExceptionInApplication("User with this username exist", ExceptionType.NOT_FOUND);
        }
    }

    private void checkUserWithEmailExists(String email) {
        if (userClient.getUserByEmail(email).isPresent()) {
            throw new ExceptionInApplication("User with this email exist", ExceptionType.NOT_FOUND);
        }
    }
}
