package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewUserResult;

import java.util.List;
import java.util.UUID;

public record ViewUserResponse (List<UserResponse> users){
    record UserResponse(UUID id, String name, String email, String role){}

    public  static ViewUserResponse from(ViewUserResult users){
        return new ViewUserResponse(users.users().stream().map(user -> new UserResponse(user.id(), user.name(), user.email(), user.role().getDescription())).toList());
    }
}
