package org.harvest.application.dto.result;

import org.harvest.domain.User;

import java.util.List;
import java.util.UUID;

public record ViewUserResult(List<User> users) {
}
