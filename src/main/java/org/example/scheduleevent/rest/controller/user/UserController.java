package org.example.scheduleevent.rest.controller.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.scheduleevent.core.user.UserService;
import org.example.scheduleevent.public_interface.user.UpdateUserDto;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.example.scheduleevent.rest.controller.user.UserMapper.map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User")
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/profile")
    @SecurityRequirement(name = "oauth2")
    public UserProfile getProfile(JwtAuthenticationToken token) {
        var userId = token.getTokenAttributes().get("sub").toString();
        var dto = userService.getProfile(userId);
        return map(dto);
    }

    @GetMapping(path = "/profile/{userId}")
    @SecurityRequirement(name = "oauth2")
    public UserProfile getProfileByUserId(@PathVariable("userId") String userId) {
        var dto = userService.getProfile(userId);
        return map(dto);
    }
}
