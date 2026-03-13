package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.DefaultResult;

public record DeleteUserResponse(String message) {
    public static DeleteUserResponse from(DefaultResult result) {
        return new DeleteUserResponse("User deleted successfully");
    }
}
