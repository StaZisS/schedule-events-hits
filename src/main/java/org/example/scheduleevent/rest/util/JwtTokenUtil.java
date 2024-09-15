package org.example.scheduleevent.rest.util;

import org.example.scheduleevent.public_interface.user.UserInfoDto;
import org.example.scheduleevent.public_interface.user.UserRoles;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.stream.Collectors;

public class JwtTokenUtil {
    public static UserInfoDto getUserInfoFromToken(JwtAuthenticationToken token) {
        var roles = token.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> UserRoles.valueOf(authority.getAuthority().replace("ROLE_", "")))
                .collect(Collectors.toSet());
        return new UserInfoDto(
                token.getToken().getClaim("sub").toString(),
                roles,
                token.getToken().getTokenValue(),
                token.getToken().getClaim("email").toString()
        );
    }
}
