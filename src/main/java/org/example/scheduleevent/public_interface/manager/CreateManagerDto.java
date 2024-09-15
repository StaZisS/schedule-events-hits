package org.example.scheduleevent.public_interface.manager;

import org.example.scheduleevent.public_interface.user.UserInfoDto;

public record CreateManagerDto(
        Long organizationId,
        UserInfoDto userInfo
) {
}
